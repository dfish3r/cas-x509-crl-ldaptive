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

import org.jasig.cas.server.CasProtocolVersion;
import org.springframework.stereotype.Component;

/**
 * Extension to the TokenServiceAccessRequest that defines which version of the CAS protocol to validate against.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class CasTokenServiceAccessRequestImpl extends DefaultTokenServiceAccessRequestImpl {

    private final CasProtocolVersion casVersion;

    public CasTokenServiceAccessRequestImpl(final CasProtocolVersion casVersion, final String token, final String serviceId, final String remoteIpAddress, final boolean renew) {
        super(null, remoteIpAddress, renew, token, serviceId);
        this.casVersion = casVersion;
    }

    public CasProtocolVersion getCasVersion() {
        return this.casVersion;
    }
}
