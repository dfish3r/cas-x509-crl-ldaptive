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

import org.jasig.cas.server.CentralAuthenticationService;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.authentication.DefaultUserNamePasswordCredential;
import org.jasig.cas.server.authentication.UserNamePasswordCredential;
import org.jasig.cas.server.login.*;
import org.jasig.cas.server.logout.DefaultLogoutRequestImpl;
import org.jasig.cas.server.logout.LogoutRequest;
import org.jasig.cas.server.session.NotFoundSessionException;
import org.jasig.cas.server.session.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * Re-implementation of the RESTful API using Spring's Controllers.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@Controller
public class CasProtocolRestfulController {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    private final CentralAuthenticationService centralAuthenticationService;

    private final ServiceAccessRequestFactory serviceAccessRequestFactory;

    public CasProtocolRestfulController(final CentralAuthenticationService centralAuthenticationService, final ServiceAccessRequestFactory serviceAccessRequestFactory) {
        this.centralAuthenticationService = centralAuthenticationService;
        this.serviceAccessRequestFactory = serviceAccessRequestFactory;
    }

    @RequestMapping(method = RequestMethod.POST, value="/v1/tickets")
    public final void obtainTicketGrantingTicket(final HttpServletRequest request, final HttpServletResponse response, final Writer writer) throws IOException {
        final List<Credential> credentials = obtainCredentialsFrom(request);
        final LoginRequest loginRequest = new DefaultLoginRequestImpl(null, request.getRemoteAddr(), false, false, null);

        loginRequest.getCredentials().addAll(credentials);
        final LoginResponse loginResponse = this.centralAuthenticationService.login(loginRequest);
        final Session session = loginResponse.getSession();

        if (session != null) {
            final StringBuffer buffer = request.getRequestURL();
            final String location;
            synchronized(buffer) {
                buffer.append("/");
                buffer.append(session.getId());
                location = buffer.toString();
            }

            response.setHeader("Location", location);
            writer.append("<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\"><html><head><title>201 Created</title></head><body><h1>TGT Created</h1><form action=\"");
            writer.append(location);
            writer.append("\" method=\"POST\">Service:<input type=\"text\" name=\"service\" value=\"\"><br><input type=\"submit\" value=\"Submit\"></form></body></html>"); 
            response.setStatus(HttpServletResponse.SC_CREATED);

        } else {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, loginResponse.getGeneralSecurityExceptions().toString());
        }
    }

    @RequestMapping(method=RequestMethod.POST, value="/v1/tickets/{ticket}")
    public final void obtainServiceTicket(final HttpServletRequest request, final HttpServletResponse response, final Writer writer, @PathVariable final String ticket) throws IOException {
        final ServiceAccessRequest serviceAccessRequest = this.serviceAccessRequestFactory.getServiceAccessRequest(ticket, request.getRemoteAddr(), request.getParameterMap());

        System.out.println(request.getParameter("service").toString());
        try {
            final ServiceAccessResponse serviceAccessResponse = this.centralAuthenticationService.grantAccess(serviceAccessRequest);
            final String serviceTicketId = serviceAccessResponse.getAccess().getId();
            response.setHeader("Content-Type", "text/plain");
            writer.append(serviceTicketId);
        } catch (final NotFoundSessionException e) {
            logger.error(e.getMessage(),e);
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "TicketGrantingTicket could not be found.");
        } catch (final Exception e) {
            logger.error(e.getMessage(),e);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @RequestMapping(method=RequestMethod.DELETE, value="/v1/tickets/{ticket}")
    public final void deleteServiceTicket(final HttpServletResponse response, @PathVariable final String ticket) throws IOException {
        final LogoutRequest logoutRequest = new DefaultLogoutRequestImpl(ticket);
        this.centralAuthenticationService.logout(logoutRequest);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected List<Credential> obtainCredentialsFrom(final HttpServletRequest request) {
        final List<Credential> credentials = new ArrayList<Credential>();
        final UserNamePasswordCredential c = new DefaultUserNamePasswordCredential();
        final ServletRequestDataBinder binder = new ServletRequestDataBinder(c);

        if (logger.isDebugEnabled()) {
            logger.debug(request.getParameterMap().toString());
            logger.debug("Username from RestletWebRequest: " + request.getParameter("userName"));
        }

        binder.bind(request);
        credentials.add(c);

        return credentials;
    }
}
