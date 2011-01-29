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

package org.jasig.cas.server.web;

import org.jasig.cas.TestUtils;
import org.jasig.cas.server.CentralAuthenticationService;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.authentication.UserNamePasswordCredential;
import org.jasig.cas.server.login.*;
import org.jasig.cas.server.session.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
/**
 *
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class CasProtocolRestfulControllerTests {

    private CasProtocolRestfulController casProtocolRestfulController;

    private final UserNamePasswordCredential ok = TestUtils.getCredentialsWithSameUsernameAndPassword();

    private final UserNamePasswordCredential bad = TestUtils.getCredentialsWithDifferentUsernameAndPassword("haha", "duh");

    private CentralAuthenticationService centralAuthenticationService;

    final CasServiceAccessRequestImpl accessRequest = new CasServiceAccessRequestImpl("TGT", "128.0.0.1", false, false, "service", false);

    @Before
    public void setUp() throws Exception {
        final ServiceAccessRequestFactory serviceAccessRequestFactory = new CasServiceAccessRequestImplFactory();

        this.centralAuthenticationService = mock(CentralAuthenticationService.class);
        this.casProtocolRestfulController = new CasProtocolRestfulController(this.centralAuthenticationService, serviceAccessRequestFactory);
    }

    @Test
    public void obtainTicketGrantingTicketWithProperCredentials() throws Exception {
        final LoginRequest loginRequest = new DefaultLoginRequestImpl(null, "128.0.0.1", false, null);
        loginRequest.getCredentials().add(this.ok);
        final LoginResponse loginResponse = mock(LoginResponse.class);
        final Session session = mock(Session.class);
        when(session.getId()).thenReturn("foo");
        when(loginResponse.getSession()).thenReturn(session);

        when(this.centralAuthenticationService.login(loginRequest)).thenReturn(loginResponse);

        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/tickets");
        request.setRemoteAddr("128.0.0.1");
        request.addParameter("userName", "test");
        request.addParameter("password", "test");
        final MockHttpServletResponse response = new MockHttpServletResponse();

        this.casProtocolRestfulController.obtainTicketGrantingTicket(request, response, response.getWriter());

        assertNotNull(response.getHeader("Location"));
        assertTrue(response.getHeader("Location").toString().contains("foo"));
        assertEquals(HttpServletResponse.SC_CREATED, response.getStatus());
    }

    @Test
    public void obtainTicketGrantingTicketWithoutProperCredentials() throws Exception {
        final LoginRequest loginRequest = new DefaultLoginRequestImpl(null, "128.0.0.1", false, null);
        loginRequest.getCredentials().add(this.bad);
        final LoginResponse loginResponse = mock(LoginResponse.class);
        when(loginResponse.getSession()).thenReturn(null);

        when(centralAuthenticationService.login(loginRequest)).thenReturn(loginResponse);

        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/tickets");
        final MockHttpServletResponse response = new MockHttpServletResponse();

        request.setRemoteAddr("128.0.0.1");
        request.addParameter("userName", "haha");
        request.addParameter("password", "duh");


        this.casProtocolRestfulController.obtainTicketGrantingTicket(request, response, response.getWriter());

        assertNull(response.getHeader("Location"));
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }

    @Test
    public void obtainServiceTicketWithValidSession() throws Exception {
        final ServiceAccessResponse serviceResponse = mock(ServiceAccessResponse.class);



        final AccessResponseResult accessResponseResult = mock(AccessResponseResult.class);
        when(accessResponseResult.getOperationToPerform()).thenReturn(AccessResponseResult.Operation.REDIRECT);
        final Map<String, List<String>> parameters = new HashMap<String, List<String>>();
        parameters.put("ticket", Arrays.asList("ticketId"));
        when(accessResponseResult.getParameters()).thenReturn(parameters);

        when(centralAuthenticationService.grantAccess(accessRequest)).thenReturn(serviceResponse);
        when(serviceResponse.generateResponse(Matchers.<AccessResponseRequest>any())).thenReturn(accessResponseResult);

        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/tickets/TGT");
        final MockHttpServletResponse response = new MockHttpServletResponse();

        request.setRemoteAddr("128.0.0.1");
        request.addParameter("service", "service");

        this.casProtocolRestfulController.obtainServiceTicket(request, response, response.getWriter(), "TGT");

        final String content = response.getContentAsString();

        assertNotNull(content);
        assertEquals(HttpServletResponse.SC_OK, response.getStatus());
        assertTrue(content.contains("ticketId"));
    }

    @Test
    public void failToObtainServiceTicketBecauseOfInvalidSession() {

    }

    @Test
    public void failToObtainServiceTicketMissingParameter() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest("POST", "/v1/tickets/TGT");
        final MockHttpServletResponse response = new MockHttpServletResponse();

        request.setRemoteAddr("128.0.0.1");

        this.casProtocolRestfulController.obtainServiceTicket(request, response, response.getWriter(), "TGT");
        assertEquals(HttpServletResponse.SC_BAD_REQUEST, response.getStatus());
    }
}
