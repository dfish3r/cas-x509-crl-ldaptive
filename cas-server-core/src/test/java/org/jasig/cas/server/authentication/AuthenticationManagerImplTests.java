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

package org.jasig.cas.server.authentication;

import org.jasig.cas.AbstractCentralAuthenticationServiceTest;
import org.jasig.cas.TestUtils;
import org.jasig.cas.server.authentication.UrlCredentialAuthenticationHandler;
import org.jasig.cas.server.authentication.SimpleTestUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentialsToPrincipalResolver;
import org.jasig.cas.server.authentication.*;
import org.jasig.cas.util.HttpClient;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class AuthenticationManagerImplTests extends AbstractCentralAuthenticationServiceTest {

    @Test
    public void testSuccessfulAuthentication() throws Exception {
        assertEquals(TestUtils.getPrincipal(), getAuthenticationManager().authenticate(TestUtils.getAuthenticationRequest(TestUtils.getCredentialsWithSameUsernameAndPassword())).getPrincipal());
    }

    @Test
    public void testFailedAuthentication() throws Exception {
        getAuthenticationManager().authenticate(TestUtils.getAuthenticationRequest(TestUtils.getCredentialsWithDifferentUsernameAndPassword()));
        fail("Authentication should have failed.");
    }

    @Test
    public void testNoHandlerFound() {
        getAuthenticationManager().authenticate(TestUtils.getAuthenticationRequest(new Credential() {

            private static final long serialVersionUID = -4897240037527663222L;
            // there is nothing to do here
        }));
        fail("Authentication should have failed.");
    }

    @Test
    public void testNoResolverFound() throws Exception {


        DefaultAuthenticationManagerImpl manager = new DefaultAuthenticationManagerImpl(Arrays.asList((AuthenticationHandler) new SimpleTestUsernamePasswordAuthenticationHandler()), Arrays.asList((CredentialToPrincipalResolver) new UsernamePasswordCredentialsToPrincipalResolver(new AttributePrincipalFactory() {
            public AttributePrincipal getAttributePrincipal(final String name) {
                return new AttributePrincipal() {

                    public String getName() {
                        return name;
                    }

                    public List<Object> getAttributeValues(String attribute) {
                        return Collections.emptyList();
                    }

                    public Object getAttributeValue(final String attribute) {
                        return null;
                    }

                    public Map<String, List<Object>> getAttributes() {
                        return Collections.emptyMap();
                    }
                };
            }
        })), new AuthenticationFactory() {
            public Authentication getAuthentication(final Map<String, List<Object>> authenticationMetaData, final AuthenticationRequest authenticationRequest, final String authenticationType) {
                return new Authentication() {

                    private Date date = new Date();

                    public Date getAuthenticationDate() {
                        return date;
                    }

                    public Map<String, List<Object>> getAuthenticationMetaData() {
                        return authenticationMetaData;
                    }

                    public boolean isLongTermAuthentication() {
                        return false;
                    }

                    public String getAuthenticationMethod() {
                        return authenticationType;
                    }
                };
            }
        });
        UrlCredentialAuthenticationHandler authenticationHandler = new UrlCredentialAuthenticationHandler(new HttpClient());
        manager.authenticate(TestUtils.getAuthenticationRequest(TestUtils.getHttpBasedServiceCredentials()));
    }

    @Test
    public void testResolverReturnsNull() throws Exception {
        DefaultAuthenticationManagerImpl manager = new DefaultAuthenticationManagerImpl(Arrays.asList((AuthenticationHandler) new SimpleTestUsernamePasswordAuthenticationHandler()), Arrays.asList((CredentialToPrincipalResolver) new UsernamePasswordCredentialsToPrincipalResolver(new AttributePrincipalFactory() {
            public AttributePrincipal getAttributePrincipal(final String name) {
                return new AttributePrincipal() {

                    public String getName() {
                        return name;
                    }

                    public List<Object> getAttributeValues(String attribute) {
                        return Collections.emptyList();
                    }

                    public Object getAttributeValue(final String attribute) {
                        return null;
                    }

                    public Map<String, List<Object>> getAttributes() {
                        return Collections.emptyMap();
                    }
                };
            }
        })), new AuthenticationFactory() {
            public Authentication getAuthentication(final Map<String, List<Object>> authenticationMetaData, final AuthenticationRequest authenticationRequest, final String authenticationType) {
                return new Authentication() {

                    private Date date = new Date();

                    public Date getAuthenticationDate() {
                        return date;
                    }

                    public Map<String, List<Object>> getAuthenticationMetaData() {
                        return authenticationMetaData;
                    }

                    public boolean isLongTermAuthentication() {
                        return false;
                    }

                    public String getAuthenticationMethod() {
                        return authenticationType;
                    }
                };
            }
        });

        UrlCredentialAuthenticationHandler authenticationHandler = new UrlCredentialAuthenticationHandler(new HttpClient());
        manager.authenticate(TestUtils.getAuthenticationRequest(TestUtils.getHttpBasedServiceCredentials()));
    }
    
    protected class TestCredentialsToPrincipalResolver implements CredentialToPrincipalResolver {

        public AttributePrincipal resolve(Credential credentials) {
            return null;
        }

        public boolean supports(final Credential credentials) {
            return true;
        }
    }
}
