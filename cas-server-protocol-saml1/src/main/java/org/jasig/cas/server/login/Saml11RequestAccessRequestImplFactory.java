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
import org.jasig.cas.server.Saml11Profile;
import org.jasig.cas.server.session.Protocol;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;
import java.util.Map;

/**
 * Constructs a new SAML11 request.  We allow you to specify which protocol you want to use via the {@link #setProfile(Saml11Profile)}
 * method. We provide a default one {@link #DEFAULT_PROFILE} if you don't choose one.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
@Named("saml1ArtifactRequestAccessFactory")
@Protocol(Protocol.ProtocolType.SAML11)
@Singleton
public final class Saml11RequestAccessRequestImplFactory extends AbstractServiceAccessRequestFactory {

    private static final String CONST_SERVICE_PARAM = "TARGET";

    private static final Saml11Profile DEFAULT_PROFILE = Saml11Profile.BrowserArtifact;

    // TODO this should be changed so we read it from some meta data file or from the Services Registry.
    @NotNull
    private Saml11Profile profile = DEFAULT_PROFILE;

    public ServiceAccessRequest getServiceAccessRequest(final String sessionId, final String remoteIpAddress, final Map parameters) {
        final String service = getValue(parameters.get(CONST_SERVICE_PARAM));

        if (StringUtils.isBlank(service)) {
            return null;
        }

        return new Saml11RequestAccessRequestImpl(sessionId, remoteIpAddress, false, false, service, this.profile);
    }

    public void setProfile(final Saml11Profile profile) {
        this.profile = profile;
    }
}
