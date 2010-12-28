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

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.server.session.Protocol;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.Map;

/**
 * Constructs a new SAML1 request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@Named("saml1ArtifactRequestAccessFactory")
@Protocol(Protocol.ProtocolType.SAML1)
@Singleton
public final class Saml1ArtifactRequestAccessRequestImplFactory extends AbstractServiceAccessRequestFactory {

    private static final String CONST_SERVICE_PARAM = "TARGET";

    public ServiceAccessRequest getServiceAccessRequest(final String sessionId, final String remoteIpAddress, final Map parameters) {
        final String service = getValue(parameters.get(CONST_SERVICE_PARAM));

        if (StringUtils.isBlank(service)) {
            return null;
        }

        return new Saml1ArtifactRequestAccessRequestImpl(sessionId, remoteIpAddress, false, false, service);
    }
}
