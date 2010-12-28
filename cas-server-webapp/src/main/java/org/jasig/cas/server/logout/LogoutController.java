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

package org.jasig.cas.server.logout;

import org.jasig.cas.server.CentralAuthenticationService;
import org.jasig.cas.server.util.SessionCookieGenerator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

/**
 * Handles logging out of the Central Authentication Service.
 * <p>
 * Cas3 supports the notion of service redirects and url parameters for linking back.  In CAS3, the service redirect was
 * handled by this controller.  In CAS4, due to the possibility that some logout will occur at the view level, we have
 * moved the logic for redirecting to the view level.  You will NOT find it in this code.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@Controller
public final class LogoutController {

    @NotNull
    private final SessionCookieGenerator sessionCookieGenerator;

    @NotNull
    private CentralAuthenticationService centralAuthenticationService;

    private boolean followServiceRedirects = false;

    @Inject
    public LogoutController(final CentralAuthenticationService centralAuthenticationService, final SessionCookieGenerator sessionCookieGenerator) {
        this.centralAuthenticationService = centralAuthenticationService;
        this.sessionCookieGenerator = sessionCookieGenerator;
    }

@RequestMapping(value = "/logout", method = RequestMethod.GET)
    public ModelMap logout(final HttpServletRequest request, final HttpServletResponse response) {
        final String sessionId = this.sessionCookieGenerator.retrieveCookieValue(request);
        final LogoutRequest logoutRequest = new DefaultLogoutRequestImpl(sessionId);
        final LogoutResponse logoutResponse = this.centralAuthenticationService.logout(logoutRequest);

        final ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("logoutResponse", logoutResponse);
        modelMap.addAttribute("followServiceRedirects", this.followServiceRedirects);

        return modelMap;
    }

    public void setFollowServiceRedirects(final boolean followServiceRedirects) {
        this.followServiceRedirects = followServiceRedirects;
    }
}
