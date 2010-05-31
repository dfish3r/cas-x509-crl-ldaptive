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
package org.jasig.cas.server.util;

import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.context.ExternalContext;
import org.springframework.webflow.context.servlet.ServletExternalContext;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public final class WebUtils {

    private WebUtils() {
		// nothing to do
	}

    public static String getCookieValue(final String cookieName, final HttpServletRequest request) {
        final Cookie cookie = org.springframework.web.util.WebUtils.getCookie(request, cookieName);
        return cookie != null ? cookie.getValue() : null;
    }

    public static String getCookieValue(final CookieGenerator cookieGenerator, final ExternalContext externalContext) {
		final HttpServletRequest request = getHttpRequest(externalContext);
		final Cookie cookie = org.springframework.web.util.WebUtils.getCookie(request, cookieGenerator.getCookieName());
		return cookie == null ? null : cookie.getValue();
	}

    /**
     * Retrieves the HttpServletRequest from the native request.
     * @param externalContext the Spring Web Flow External Context.
     * @return the HttpServletResponse. CANNOT be NULL.
     */
    private static HttpServletRequest getHttpRequest(final ExternalContext externalContext) {
		final ServletExternalContext servletExternalContext = (ServletExternalContext) externalContext;
		return (HttpServletRequest) servletExternalContext.getNativeRequest();
	}

    /**
     * Retrieves the HttpServletResponse from the native request.
     * @param externalContext the Spring Web Flow External Context.
     * @return the HttpServletResponse. CANNOT be NULL.
     */
    private static HttpServletResponse getHttpResponse(final ExternalContext externalContext) {
        final ServletExternalContext servletExternalContext = (ServletExternalContext) externalContext;
        return (HttpServletResponse) servletExternalContext.getNativeResponse();
    }

    public static void setCookieValue(final CookieGenerator cookieGenerator, final ExternalContext externalContext, final String value) {
        final HttpServletResponse response = getHttpResponse(externalContext);
        cookieGenerator.addCookie(response, value);
    }
}
