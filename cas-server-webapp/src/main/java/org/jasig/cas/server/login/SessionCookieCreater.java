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

package org.jasig.cas.server.login;

import org.jasig.cas.server.util.WebUtils;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.context.ExternalContext;

import javax.validation.constraints.NotNull;

/**
 * Creates and saves the session cookie in the response.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class SessionCookieCreater {

    @NotNull
    private final CookieGenerator cookieGenerator;

    public SessionCookieCreater(final CookieGenerator cookieGenerator) {
        this.cookieGenerator = cookieGenerator;
    }

    public boolean createSessionCookie(final LoginResponse loginResponse, final ServiceAccessResponse serviceAccessResponse, final ExternalContext externalContext) {
        final String sessionId = serviceAccessResponse != null ? serviceAccessResponse.getSession().getId() : loginResponse.getSession().getId();
        WebUtils.setCookieValue(this.cookieGenerator, externalContext, sessionId);

        return true;
    }
}
