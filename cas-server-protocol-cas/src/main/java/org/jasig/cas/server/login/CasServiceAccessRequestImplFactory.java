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

import org.springframework.util.StringUtils;

import java.util.Map;

/**
 * Implementation creates new {@link org.jasig.cas.server.login.CasServiceAccessRequestImpl}.
 * <p>
 * This supports requesting a POST response via using parameter method=POST.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class CasServiceAccessRequestImplFactory extends  AbstractServiceAccessRequestFactory {

    public ServiceAccessRequest getServiceAccessRequest(final String sessionId, final String remoteIpAddress, final Map parameters) {
        final String serviceId = getValue(parameters.get("service"));
        final boolean gateway = StringUtils.hasText(getValue(parameters.get("gateway")));
        final boolean renew = StringUtils.hasText((getValue(parameters.get("renew"))));
        final boolean post = "POST".equals(getValue(parameters.get("method")));

        if (!StringUtils.hasText(serviceId)) {
            return null;
        }

        return new CasServiceAccessRequestImpl(sessionId, remoteIpAddress, renew, gateway, serviceId, post);
    }
}
