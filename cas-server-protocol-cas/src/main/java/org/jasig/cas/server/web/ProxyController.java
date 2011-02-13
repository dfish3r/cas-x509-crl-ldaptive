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
import org.jasig.cas.server.login.CasServiceAccessRequestImpl;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.jasig.cas.server.login.ServiceAccessResponse;
import org.jasig.cas.server.session.AccessResponseResult;
import org.jasig.cas.server.session.DefaultAccessResponseRequestImpl;
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
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
@Controller("proxyController")
public final class ProxyController extends ApplicationObjectSupport {

    @NotNull
    private final CentralAuthenticationService centralAuthenticationService;

    @Inject
    public ProxyController(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    @RequestMapping(method= RequestMethod.GET, value="/proxy")
    public ModelAndView generateProxyRequest(@RequestParam(value="pgt") final String pgt, @RequestParam(value="targetService") final String service, final HttpServletRequest request, final Writer writer) throws IOException {
        final ServiceAccessRequest serviceAccessRequest = new CasServiceAccessRequestImpl(pgt, service, request.getRemoteAddr());

        final ServiceAccessResponse serviceAccessResponse = this.centralAuthenticationService.grantAccess(serviceAccessRequest);
        final AccessResponseResult result = serviceAccessResponse.generateResponse(new DefaultAccessResponseRequestImpl(writer));

        switch (result.getOperationToPerform()) {
            case ERROR_VIEW:
                final ModelAndView errorView = new ModelAndView();

                errorView.setViewName(result.getViewName());
                errorView.addObject("code", result.getCode());
                errorView.addObject("description", getMessageSourceAccessor().getMessage(result.getMessageCode(), new Object[] {serviceAccessRequest.getServiceId()}, result.getMessageCode()));
                return errorView;

            case VIEW:
                return new ModelAndView(result.getViewName(), result.getModelMap());

            default:
                return null;
        }

    }
}
