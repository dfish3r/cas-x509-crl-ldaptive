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

import com.github.inspektr.common.web.ClientInfoHolder;
import org.jasig.cas.server.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.util.CookieGenerator;
import org.springframework.webflow.execution.RequestContext;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Map;

/**
 * Constructs a new LoginRequest based on parameters supplied as part of the request.
 * <p>
 * This is essentially the same as iterating through all of the argument extractors in CAS3.4
 * <p>
 * If it can't construct a {@link org.jasig.cas.server.login.ServiceAccessRequest} it creates a default, non-service
 * login request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class LoginRequestFactory {

    @NotNull
	private final CookieGenerator cookieGenerator;

    @NotNull
    @Size(min=1)
    private final List<ServiceAccessRequestFactory> serviceAccessRequestFactories;

    @Autowired(required=true)
    public LoginRequestFactory(@Qualifier("tgt") final CookieGenerator cookieGenerator, final List<ServiceAccessRequestFactory> serviceAccessRequestFactories) {
        this.cookieGenerator = cookieGenerator;
        this.serviceAccessRequestFactories = serviceAccessRequestFactories;
    }

	public LoginRequest createLoginRequest(final RequestContext context) throws Exception {
        final String remoteIpAddress = ClientInfoHolder.getClientInfo().getClientIpAddress();
		final String sessionIdentifier = WebUtils.getCookieValue(this.cookieGenerator, context.getExternalContext());
		final Map parameters = context.getRequestParameters().asMap();

        for (final ServiceAccessRequestFactory serviceAccessRequestFactory : this.serviceAccessRequestFactories) {
            final LoginRequest loginRequest = serviceAccessRequestFactory.getServiceAccessRequest(sessionIdentifier, remoteIpAddress, parameters);

            if (loginRequest != null) {
                return loginRequest;
            }
        }

        return new DefaultLoginRequestImpl(sessionIdentifier, remoteIpAddress, false, false,null);
	}
}
