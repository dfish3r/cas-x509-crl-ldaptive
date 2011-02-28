/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.cas.server.session;

import junit.framework.TestCase;
import org.jasig.cas.TestUtils;
import org.jasig.cas.server.authentication.*;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.jasig.cas.server.login.TokenServiceAccessRequest;
import org.jasig.cas.server.util.Cleanable;
import org.junit.Before;
import org.junit.Test;
import org.springframework.transaction.annotation.Transactional;

import static org.mockito.Mockito.*;

import java.security.GeneralSecurityException;
import java.util.*;

/**
 * Abstract class to ensure all session storage are run against the same tests.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@Transactional
public abstract class AbstractSessionStorageTests extends TestCase {

    private final AttributePrincipalFactory attributePrincipalFactory = getAttributePrincipalFactory();

    private final AuthenticationFactory authenticationFactory = getAuthenticationFactory();

    private SessionStorage sessionStorage;

    protected abstract SessionStorage getSessionStorage();

    protected abstract AuthenticationFactory getAuthenticationFactory();

    protected abstract AttributePrincipalFactory getAttributePrincipalFactory();

    protected final AuthenticationResponse getAuthenticationResponse(final String principal) {
        final AuthenticationResponse response = mock(AuthenticationResponse.class);
        final HashSet<Authentication> authentications = new HashSet<Authentication>();
        authentications.addAll(Arrays.asList(getConstructedAuthentication()));
        when(response.getAuthentications()).thenReturn(authentications);
        when(response.getPrincipal()).thenReturn(this.attributePrincipalFactory.getAttributePrincipal(principal));
        when(response.getGeneralSecurityExceptions()).thenReturn(Collections.<Credential, List<GeneralSecurityException>>emptyMap());
        when(response.getAuthenticationMessages()).thenReturn(Collections.<Message>emptyList());

        return response;
    }

    protected final Authentication getConstructedAuthentication() {
        final AuthenticationRequest authenticationRequest = mock(AuthenticationRequest.class);
        when(authenticationRequest.getAuthenticationRequestDate()).thenReturn(new Date());
        when(authenticationRequest.getCredentials()).thenReturn(Collections.<Credential>emptyList());
        when(authenticationRequest.isLongTermAuthenticationRequest()).thenReturn(false);
        return this.authenticationFactory.getAuthentication(Collections.<String, List<Object>>emptyMap(), authenticationRequest, "myMethod");
    }

    protected Class<? extends Access> getAccessClass() {
        return Access.class;
    }

    protected final ExpirationPolicy getExpirationPolicy() {
        return new ExpirationPolicy() {
            public boolean isExpired(final State state) {
                return false;
            }
        };
    }

    protected final List<AccessFactory> getAccessFactories() {
        final List<AccessFactory> accessFactories = new ArrayList<AccessFactory>();

        accessFactories.add(new AccessFactory() {
            public Access getAccess(final Session session, final ServiceAccessRequest serviceAccessRequest) {
                if (getAccessClass().equals(Access.class)) {
                    final String id = UUID.randomUUID().toString();
                    return new MockAccess(id, serviceAccessRequest.getServiceId());
                } else {
                    final Access access = mock(getAccessClass());

                    final String serviceId = serviceAccessRequest.getServiceId();
                    when(access.getResourceIdentifier()).thenReturn(serviceId);
                    when(access.getId()).thenReturn(UUID.randomUUID().toString());
                    return access;
                }
            }
        });

        return accessFactories;
    }


    @Before
    public void setUp() throws Exception {
        this.sessionStorage = getSessionStorage();
    }

    @Test
    public final void testGrantSaveAndRetrieveAccess() {
        final Session session = this.sessionStorage.createSession(getAuthenticationResponse("test1"));
        final ServiceAccessRequest serviceAccessRequest = mock(ServiceAccessRequest.class);
        when(serviceAccessRequest.getServiceId()).thenReturn("http://www.cnn.com");
        final Access access = session.grant(serviceAccessRequest);
        this.sessionStorage.updateSession(session);
        assertNull(this.sessionStorage.findSessionByAccessId("foobar"));
        assertNotNull(this.sessionStorage.findSessionByAccessId(access.getId()));
    }

    @Test
    public final void testCreateSession() {
        final Session session = this.sessionStorage.createSession(getAuthenticationResponse("test1"));
        assertNotNull(session);
    }

    @Test
    public final void testDestroySessionThatExists() {
        final Session session = this.sessionStorage.createSession(getAuthenticationResponse("test2"));
        assertNotNull(session);

        assertNotNull(this.sessionStorage.findSessionBySessionId(session.getId()));

        assertNotNull(this.sessionStorage.destroySession(session.getId()));
        assertNull(this.sessionStorage.destroySession(session.getId()));
    }

    @Test
    public final void testDestroySessionThatDoesNotExist() {
        assertNull(this.sessionStorage.destroySession("sessionThatDoesNotExist"));
    }

    @Test
    public final void testRetrieveRootSessionThatExists() {
        final Session session = this.sessionStorage.createSession(getAuthenticationResponse("test3"));
        assertNotNull(session);

        final Session session2 = this.sessionStorage.findSessionBySessionId(session.getId());
        assertNotNull(session2);
        
        assertEquals(session, session2);
    }

    @Test
    public final void testRetrieveRootSessionThatDoesNotExist() {
        final Session session = this.sessionStorage.createSession(getAuthenticationResponse("test4"));
        assertNotNull(session);

        final Session session2 = this.sessionStorage.findSessionBySessionId("FOOBAR");
        assertNull(session2);
    }

    @Test
    public final void testRetrieveSessionsForUserThatDoesNotExist() {
        this.sessionStorage.createSession(getAuthenticationResponse("test5"));
        this.sessionStorage.createSession(getAuthenticationResponse("test5"));

        final Set<Session> sessions = this.sessionStorage.findSessionsByPrincipal("FOOBAR");

        assertTrue(sessions.isEmpty());
    }

    @Test
    public final void testRetrieveSessionsForUserThatDoestExist() {
        this.sessionStorage.createSession(getAuthenticationResponse("test"));
        this.sessionStorage.createSession(getAuthenticationResponse("test"));
        this.sessionStorage.createSession(getAuthenticationResponse("FOOBAR"));

        assertEquals(2, this.sessionStorage.findSessionsByPrincipal("test").size());
        assertEquals(1, this.sessionStorage.findSessionsByPrincipal("FOOBAR").size());
    }

    // TODO retrieve by access, as well as delegated sessions

    @Test
    public final void testPurge() {
        getSessionStorage().purge();
    }

    @Test
    public final void testRetrieveStats() {
        getSessionStorage().getSessionStorageStatistics();
    }

    @Test
    public final void testCleanable() {
        final SessionStorage sessionStorage = getSessionStorage();

        if (sessionStorage instanceof AbstractSessionStorage) {
            ((AbstractSessionStorage) sessionStorage).setExpirationPolicy(new ExpirationPolicy() {
                public boolean isExpired(State state) {
                    return true;
                }
            });
        }

        final Session s = sessionStorage.createSession(new AuthenticationResponse() {
            public boolean succeeded() {
                return true;
            }

            public Set<Authentication> getAuthentications() {
                final HashSet<Authentication> hs = new HashSet<Authentication>();
                hs.add(getAuthenticationFactory().getAuthentication(Collections.<String, List<Object>>emptyMap(), TestUtils.getAuthenticationRequest(TestUtils.getCredentialsWithSameUsernameAndPassword()), "foo"));
                hs.add(getAuthenticationFactory().getAuthentication(Collections.<String, List<Object>>emptyMap(), TestUtils.getAuthenticationRequest(TestUtils.getCredentialsWithSameUsernameAndPassword()), "bar"));
                return hs;
            }

            public AttributePrincipal getPrincipal() {
                return getAttributePrincipalFactory().getAttributePrincipal("test");
            }

            public Map<Credential, List<GeneralSecurityException>> getGeneralSecurityExceptions() {
                return Collections.emptyMap();
            }

            public List<Message> getAuthenticationMessages() {
                return Collections.emptyList();
            }

            public Map<String, Object> getAttributes() {
                return Collections.emptyMap();
            }
        });

        if (sessionStorage instanceof Cleanable) {
            ((Cleanable) sessionStorage).prune();
        }
    }
}
