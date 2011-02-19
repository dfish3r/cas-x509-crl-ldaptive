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

package org.jasig.cas.web.flow;

import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.login.LoginRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.execution.RequestContext;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Abstract class to handle the retrieval and authentication of non-interactive
 * credentials such as client certificates, NTLM, etc.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.4
 */
public abstract class AbstractNonInteractiveCredentialsAction {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public final boolean addCredential(final RequestContext requestContext, final LoginRequest loginRequest) {
        final Credential credentials = constructCredentialsFromRequest(getHttpServletRequest(requestContext), getHttpServletResponse(requestContext));

        if (credentials == null) {
            return false;
        }

        loginRequest.getCredentials().add(credentials);
        return true;
    }

   public static HttpServletRequest getHttpServletRequest(final RequestContext context) {
        Assert.isInstanceOf(ServletExternalContext.class, context.getExternalContext(),
                String.format("Cannot obtain HttpServletRequest from event of type: %s", context.getExternalContext().getClass().getName()));
        return (HttpServletRequest) context.getExternalContext().getNativeRequest();
    }

    public static HttpServletResponse getHttpServletResponse(final RequestContext context) {
        Assert.isInstanceOf(ServletExternalContext.class, context.getExternalContext(),
            String.format("Cannot obtain HttpServletResponse from event of type: %s", context.getExternalContext().getClass().getName()));
        return (HttpServletResponse) context.getExternalContext().getNativeResponse();
    }

    /**
     * Abstract method to implement to construct the credentials from the
     * request object.
     * 
     * @param request the request.
     * @param response the response.
     * @return the constructed credentials or null if none could be constructed
     * from the request.
     */
    protected abstract Credential constructCredentialsFromRequest(final HttpServletRequest request, HttpServletResponse response);
}
