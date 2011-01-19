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

import org.jasig.cas.server.CasProtocolVersion;
import org.jasig.cas.server.CentralAuthenticationService;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.authentication.DefaultUrlCredentialImpl;
import org.jasig.cas.server.login.CasTokenServiceAccessRequestImpl;
import org.jasig.cas.server.login.DefaultLoginRequestImpl;
import org.jasig.cas.server.login.LoginRequest;
import org.jasig.cas.server.login.LoginResponse;
import org.jasig.cas.server.session.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * Handles the request to validate the various different CAS protocols.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
@Controller("validationController")
public final class ValidationController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @NotNull
    private final CentralAuthenticationService centralAuthenticationService;

    @Inject
    public ValidationController(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    @RequestMapping(method=RequestMethod.GET, value="/**/validate")
    public final ModelAndView validateCas10Request(@RequestParam(value="renew",required=false, defaultValue = "false") final boolean renew, @RequestParam(value="service",required=true) final String service, @RequestParam(value="ticket",required=true) final String ticket, final HttpServletRequest request, final Writer writer) throws IOException {
        return validateRequest(renew, service, ticket, CasProtocolVersion.CAS1, request, writer);
    }

    @RequestMapping(method=RequestMethod.GET, value="/**/serviceValidate")
    public final ModelAndView validateCas20Request(@RequestParam(value="renew",required=false,defaultValue = "false") final boolean renew, @RequestParam(value="service",required=true) final String service, @RequestParam(value="ticket",required=true) final String ticket, @RequestParam(value="pgtUrl",required=false) final String pgtUrl, final HttpServletRequest request, final Writer writer) {
        return validateRequest(renew, service, ticket, CasProtocolVersion.CAS2, request, writer);
    }

    @RequestMapping(method= RequestMethod.GET, value="/**/proxyValidate")
    public final ModelAndView validateCasProxyTicketRequest(@RequestParam(value="renew",required=false,defaultValue = "false") final boolean renew, @RequestParam(value="service",required=true) final String service, @RequestParam(value="ticket",required=true) final String ticket, @RequestParam(value="pgtUrl",required=false) final String pgtUrl, final HttpServletRequest request, final Writer writer) {
        return validateRequest(renew, service, ticket, CasProtocolVersion.CAS2_WITH_PROXYING, request, writer);
    }

    protected final ModelAndView validateRequest(final boolean renew, final String service, final String ticket, final CasProtocolVersion casVersion, final HttpServletRequest request, final Writer writer) {

        final CasTokenServiceAccessRequestImpl casTokenServiceAccessRequest = new CasTokenServiceAccessRequestImpl(casVersion, ticket, service, request.getRemoteAddr(), renew);
        final Access access = this.centralAuthenticationService.validate(casTokenServiceAccessRequest);

        logger.debug(String.format("Successfully validated: %s", ticket));
        final Session proxySession;
        final Credential proxyCredential = createProxyCredential(request);
        if (proxyCredential != null) {
            final LoginRequest loginRequest = new DefaultLoginRequestImpl(null, request.getRemoteAddr(), false, access);
            loginRequest.getCredentials().add(proxyCredential);
            final LoginResponse loginResponse = this.centralAuthenticationService.login(loginRequest);
            proxySession = loginResponse.getSession();
        } else {
            proxySession = null;
        }
// TODO revisit this
        final AccessResponseRequest accessResponseRequest = new DefaultAccessResponseRequestImpl(writer, proxySession != null ? proxySession.getId() : null, proxyCredential);
        final AccessResponseResult accessResponseResult = access.generateResponse(accessResponseRequest);

        if (!AccessResponseResult.Operation.VIEW.equals(accessResponseResult.getOperationToPerform())) {
            final ModelAndView modelAndView = new ModelAndView();
            return modelAndView;
        }

        return null;
    }

    protected Credential createProxyCredential(final HttpServletRequest request) {
        final String pgtUrl = request.getParameter("pgtUrl");

        return pgtUrl != null ? new DefaultUrlCredentialImpl(pgtUrl) : null;
    }

    protected final void writeErrorResponse(final String errorCode, final String errorMessage, final CasProtocolVersion casVersion, final Writer writer) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("errorCode", errorCode);
        parameters.put("message", errorMessage);
//        FreemarkerUtils.writeToFreeMarkerTemplate(casVersion.asString() + "errorResponseTemplate.ftl", parameters, writer);
    }

}
