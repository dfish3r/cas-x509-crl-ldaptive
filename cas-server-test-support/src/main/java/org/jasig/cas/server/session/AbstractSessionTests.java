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

import org.jasig.cas.server.authentication.AttributePrincipal;
import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * Abstract tests for the {@link org.jasig.cas.server.session.Session} interface.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSessionTests {

    private Session session;

    private Authentication authentication;

    private AttributePrincipal attributePrincipal;

    private ServicesManager servicesManager = new ServicesManager() {
        public void save(RegisteredService registeredService) {
            // nothing to do
        }

        public RegisteredService delete(long id) {
            return null;
        }

        public RegisteredService findServiceBy(Access access) {
            return findServiceBy(1L);
        }

        public RegisteredService findServiceBy(long id) {
            final RegisteredService r = mock(RegisteredService.class);
            when(r.isSsoEnabled()).thenReturn(true);
            when(r.isEnabled()).thenReturn(true);
            when(r.isAllowedToProxy()).thenReturn(true);
            return r;
        }

        public Collection<RegisteredService> getAllServices() {
            return null;
        }

        public boolean matchesExistingService(Access service) {
            return true;
        }
    };

    /**
     * Returns a new Session.
     *
     * @return a new session.  CANNOT be NULL.
     */
    protected abstract Session getNewSession(Authentication authentication, AttributePrincipal attributePrincipal, ServicesManager servicesManager);

    protected abstract Authentication getNewAuthentication();

    protected abstract AttributePrincipal getNewAttributePrincipal();

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
                final Access access = mock(Access.class);

                final String serviceId = serviceAccessRequest.getServiceId();
                when(access.getResourceIdentifier()).thenReturn(serviceId);
                when(access.getId()).thenReturn(UUID.randomUUID().toString());
                when(access.requiresStorage()).thenReturn(true);
                return access;
            }
        });

        return accessFactories;
    }

    @Before
    public final void setUp() throws Exception {
        this.authentication = getNewAuthentication();
        this.attributePrincipal = getNewAttributePrincipal();
        this.session = getNewSession(this.authentication, this.attributePrincipal, this.servicesManager);
    }

    @Test
    public final void notNullId() {
        assertNotNull(this.session.getId());
    }

    @Test
    public final void uniqueId() {
        final Session s = getNewSession(this.authentication, this.attributePrincipal, this.servicesManager);
        assertNotSame(s.getId(), this.session.getId());
    }

    @Test
    public final void areAuthenticationsThere() {
        assertNotNull(this.session.getAuthentications());
        assertFalse(this.session.getAuthentications().isEmpty());
        assertTrue(this.session.getAuthentications().contains(this.authentication));
    }

    @Test
    public final void areRootAuthenticationsThere() {
        assertNotNull(this.session.getRootAuthentications());
        assertFalse(this.session.getRootAuthentications().isEmpty());
        assertTrue(this.session.getRootAuthentications().contains(this.authentication));
    }

    @Test
    public final void isPrincipalThere() {
        assertNotNull(this.session.getPrincipal());
        assertEquals(this.attributePrincipal, this.session.getPrincipal());
    }

    @Test
    public final void isRootPrincipalThere() {
        assertNotNull(this.session.getRootPrincipal());
        assertEquals(this.attributePrincipal, this.session.getRootPrincipal());
    }

    @Test
    public final void isRootSession() {
        assertEquals(this.session, this.session.getRootSession());
    }

    @Test
    public final void isRoot() {
         assertTrue(this.session.isRoot());
    }

    @Test
    public final void validity() {
        assertTrue(this.session.isValid());
        this.session.invalidate();
        assertFalse(this.session.isValid());
    }

    @Test
    public final void hasBeenUsed() throws SessionException {
        assertTrue(this.session.hasNotBeenUsed());

        final ServiceAccessRequest serviceAccessRequest = mock(ServiceAccessRequest.class);
        when(serviceAccessRequest.getServiceId()).thenReturn("foobar");
        this.session.grant(serviceAccessRequest);
        assertFalse(this.session.hasNotBeenUsed());
    }

    @Test
    public final void checkForAccess() throws SessionException {
        final ServiceAccessRequest serviceAccessRequest = mock(ServiceAccessRequest.class);
        when(serviceAccessRequest.getServiceId()).thenReturn("foobar");
        final Access access = this.session.grant(serviceAccessRequest);


        assertFalse(this.session.getAccesses().isEmpty());
        assertTrue(this.session.getAccesses().contains(access));
        assertEquals(access, this.session.getAccess(access.getId()));
    }

    @Test
    public final void proxiedAuthentication() {
        assertTrue(this.session.getProxiedAuthentications().isEmpty());
    } 

    @Test
    public final void proxiedPrincipal() {
        assertTrue(this.session.getProxiedPrincipals().isEmpty());
    }

    @Test
    public final void additionalAuthentication() {
        final Authentication authentication = getNewAuthentication();
        // TODO take these out later
        System.out.println("this:" + this.authentication.getAuthenticationMethod());
        System.out.println("local:" + authentication.getAuthenticationMethod());
        this.session.addAuthentication(authentication);

        assertEquals(2, this.session.getAuthentications().size());
        assertTrue(this.session.getAuthentications().contains(authentication));
    }


    @Test
    public final void delegatedSession() throws SessionException {
        final AuthenticationResponse authenticationResponse = mock(AuthenticationResponse.class);
        final Authentication authentication = getNewAuthentication();
        final Set<Authentication> authentications = new HashSet<Authentication>();

        authentications.add(authentication);
        when(authenticationResponse.getPrincipal()).thenReturn(getNewAttributePrincipal());
        when(authenticationResponse.getAuthentications()).thenReturn(authentications);
        when(authenticationResponse.succeeded()).thenReturn(true);

        final Access access = mock(Access.class);

        final Session session = this.session.createDelegatedSession(access, authenticationResponse);

        assertFalse(session.isRoot());
        assertEquals(this.session, session.getRootSession());
        assertEquals(this.attributePrincipal, session.getRootPrincipal());

        assertNotNull(session.getAuthentications());
        assertFalse(session.getAuthentications().isEmpty());
        assertTrue(session.getAuthentications().contains(authentication));

        assertNotNull(session.getRootAuthentications());
        assertFalse(session.getRootAuthentications().isEmpty());
        assertTrue(session.getRootAuthentications().contains(this.authentication));

        assertNotNull(session.getProxiedAuthentications());
        assertFalse(session.getProxiedAuthentications().isEmpty());
        // XXX assertTrue(session.getProxiedAuthentications().contains(session.getAuthentications()));
        assertFalse(session.getProxiedAuthentications().contains(this.session.getAuthentications()));

        assertNotNull(session.getProxiedPrincipals());
        assertFalse(session.getProxiedPrincipals().isEmpty());
        assertTrue(session.getProxiedPrincipals().contains(session.getPrincipal()));
        assertFalse(session.getProxiedPrincipals().contains(this.session.getPrincipal()));

        assertEquals(session, this.session.findChildSessionById(session.getId()));

        this.session.invalidate();
        assertFalse(session.isValid());

        final ServiceAccessRequest serviceAccessRequest = mock(ServiceAccessRequest.class);
        when(serviceAccessRequest.getServiceId()).thenReturn("foobar");

        try {
            this.session.grant(serviceAccessRequest);
            fail("InvalidatedSessionException expected.");
        } catch (final InvalidatedSessionException e) {
            return;
        }
    }
}
