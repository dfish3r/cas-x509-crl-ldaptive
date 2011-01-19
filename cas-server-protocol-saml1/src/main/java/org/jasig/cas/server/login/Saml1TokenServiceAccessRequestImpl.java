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

import org.jasig.cas.server.session.Access;

/**
 * Constructs a request used to validate a SAML1 request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class Saml1TokenServiceAccessRequestImpl extends DefaultTokenServiceAccessRequestImpl {

    public Saml1TokenServiceAccessRequestImpl(final String sessionId, final String remoteIpAddress, final boolean forceAuthentication, final String token, final String serviceId) {
        super(sessionId, remoteIpAddress, forceAuthentication, token, serviceId);
    }


    // TODO fix
    public boolean validate() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Access generateInvalidRequestAccess() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public Access generateInvalidSessionAccess() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
