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
import org.jasig.cas.server.login.*;
import org.jasig.cas.server.session.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.Writer;

/**
 * Handles the request to IsValid the various different CAS protocols.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
@Controller("validationController")
public final class ValidationController extends ApplicationObjectSupport {

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

        final Credential proxyCredential = createProxyCredential(request);

        if (proxyCredential != null) {
            casTokenServiceAccessRequest.getCredentials().add(proxyCredential);
        }

        final ServiceAccessResponse serviceAccessResponse = this.centralAuthenticationService.validate(casTokenServiceAccessRequest);

        logger.debug(String.format("Successfully validated: %s", ticket));
        final AccessResponseRequest accessResponseRequest = new DefaultAccessResponseRequestImpl(writer);
        final AccessResponseResult accessResponseResult = serviceAccessResponse.generateResponse(accessResponseRequest);

        switch (accessResponseResult.getOperationToPerform()) {
            case ERROR_VIEW:
                final ModelAndView errorView = new ModelAndView();

                errorView.setViewName(accessResponseResult.getViewName());
                errorView.addObject("code", accessResponseResult.getCode());
                errorView.addObject("description", getMessageSourceAccessor().getMessage(accessResponseResult.getMessageCode(), new Object[] {casTokenServiceAccessRequest.getToken(), casTokenServiceAccessRequest.getServiceId()}, accessResponseResult.getMessageCode()));
                return errorView;

            case VIEW:
                final ModelAndView view = new ModelAndView();
                view.addAllObjects(accessResponseResult.getModelMap());
                view.setViewName(accessResponseResult.getViewName());
                return view;

            default:
                return null;
        }
    }

    protected Credential createProxyCredential(final HttpServletRequest request) {
        final String pgtUrl = request.getParameter("pgtUrl");

        return pgtUrl != null ? new DefaultUrlCredentialImpl(pgtUrl) : null;
    }
}
