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

package org.jasig.cas.adaptors.trusted.web.flow;

import org.junit.Test;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.5
 *
 */
public class PrincipalFromRequestUserPrincipalNonInteractiveCredentialsActionTests {

    private PrincipalFromRequestUserPrincipalNonInteractiveCredentialsAction action;

    @Test
    public void doNothing() {
        return;
    }
    /*
    protected void setUp() throws Exception {
        this.action = new PrincipalFromRequestUserPrincipalNonInteractiveCredentialsAction();
        
        final DefaultCentralAuthenticationServiceImpl centralAuthenticationService = new DefaultCentralAuthenticationServiceImpl();
        centralAuthenticationService.setTicketRegistry(new DefaultTicketRegistry());
        final Map<String, UniqueTicketIdGenerator> idGenerators = new HashMap<String, UniqueTicketIdGenerator>();
        idGenerators.put(SimpleWebApplicationServiceImpl.class.getName(), new DefaultUniqueTicketIdGenerator());


        final DefaultAuthenticationManagerImpl authenticationManager = new DefaultAuthenticationManagerImpl();
   
        authenticationManager.setAuthenticationHandlers(Arrays.asList(new AuthenticationHandler[] {new PrincipalBearingCredentialsAuthenticationHandler()}));
        authenticationManager.setCredentialsToPrincipalResolvers(Arrays.asList(new CredentialsToPrincipalResolver[] {new PrincipalBearingCredentialsToPrincipalResolver()}));
        
        centralAuthenticationService.setTicketGrantingTicketUniqueTicketIdGenerator(new DefaultUniqueTicketIdGenerator());
        centralAuthenticationService.setUniqueTicketIdGeneratorsForService(idGenerators);
        centralAuthenticationService.setServiceTicketExpirationPolicy(new NeverExpiresExpirationPolicy());
        centralAuthenticationService.setTicketGrantingTicketExpirationPolicy(new NeverExpiresExpirationPolicy());
        centralAuthenticationService.setAuthenticationManager(authenticationManager);
        
        this.action.setCentralAuthenticationService(centralAuthenticationService);
    }
    
    public void testRemoteUserExists() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(new Principal() {public String getName() {return "test";}});
        
        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, new MockHttpServletResponse()));
        
        assertEquals("success", this.action.execute(context).getId());
    }
    
    public void testRemoteUserDoesntExists() throws Exception {
        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), new MockHttpServletRequest(), new MockHttpServletResponse()));
        
        assertEquals("error", this.action.execute(context).getId());
    }  */
    
}
