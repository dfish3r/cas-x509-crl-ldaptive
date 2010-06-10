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

package org.jasig.cas.authentication;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasig.cas.TestUtils;
import org.jasig.cas.server.authentication.DirectMappingAuthenticationManagerImpl.DirectAuthenticationHandlerMappingHolder;
import org.jasig.cas.authentication.handler.support.SimpleTestUsernamePasswordAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentialsToPrincipalResolver;

import junit.framework.TestCase;
import org.jasig.cas.server.authentication.*;


public class DirectMappingAuthenticationManagerImplTests extends TestCase {

    private DirectMappingAuthenticationManagerImpl manager = new DirectMappingAuthenticationManagerImpl();

    protected void setUp() throws Exception {
        this.manager = new DirectMappingAuthenticationManagerImpl();
        
        final Map<Class<? extends Credential>, DirectAuthenticationHandlerMappingHolder> mappings = new HashMap<Class<? extends Credential>, DirectAuthenticationHandlerMappingHolder>();
        final List<AuthenticationMetaDataResolver> populators = new ArrayList<AuthenticationMetaDataResolver>();
//        populators.add(new SamlAuthenticationMetaDataPopulator());
        
//        this.manager.setAuthenticationMetaDataPopulators(populators);
        
        final DirectAuthenticationHandlerMappingHolder d = new DirectAuthenticationHandlerMappingHolder();
        d.setAuthenticationHandler(new SimpleTestUsernamePasswordAuthenticationHandler());
        d.setCredentialToPrincipalResolver(new UsernamePasswordCredentialsToPrincipalResolver(new AttributePrincipalFactory() {
            public AttributePrincipal getAttributePrincipal(String name) {
                return null;
            }
        }));
        
        mappings.put(UserNamePasswordCredential.class, d);
        
        this.manager.setCredentialsMapping(mappings);
        super.setUp();
    }
    
    public void testAuthenticateUsernamePassword() throws Exception {
        final UserNamePasswordCredential c = new UserNamePasswordCredential() {
            public String getUserName() {
                return "Test";
            }

            public String getPassword() {
                return "Test";
            }
        };
        final AuthenticationResponse authentication = this.manager.authenticate(TestUtils.getAuthenticationRequest(c));
        
        assertEquals(c.getUserName(), authentication.getPrincipal().getName());
    }
    
    public void testAuthenticateBadUsernamePassword() throws Exception {
        final UserNamePasswordCredential c = new UserNamePasswordCredential() {
            public String getUserName() {
                return "Test";
            }

            public String getPassword() {
                return "Test2";
            }
        };
        
        this.manager.authenticate(TestUtils.getAuthenticationRequest(c));
        fail();
    }
    
    public void testAuthenticateHttp() throws Exception {
        
        try {
            final UrlCredential c = new UrlCredential() {
                public URL getUrl() {
                    try {
                        return new URL("http://www.cnn.com");
                    } catch (final Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            };
            
            this.manager.authenticate(TestUtils.getAuthenticationRequest(c));
            fail("Exception expected.");
        } catch (final IllegalArgumentException e) {
            return;
        }
    }
}
