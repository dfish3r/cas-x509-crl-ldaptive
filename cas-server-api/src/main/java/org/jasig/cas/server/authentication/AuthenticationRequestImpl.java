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

package org.jasig.cas.server.authentication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Immutable request for authentication.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class AuthenticationRequestImpl implements AuthenticationRequest {

    private final Date authenticationRequestDate = new Date();

    private final List<Credential> credentials = new ArrayList<Credential>();

    private final boolean longTermAuthenticationRequest;

    public AuthenticationRequestImpl(final List<Credential> credentials, final boolean longTermAuthenticationRequest) {
        this.credentials.addAll(credentials);
        this.longTermAuthenticationRequest = longTermAuthenticationRequest;
    }

    public Date getAuthenticationRequestDate() {
        return new Date(this.authenticationRequestDate.getTime());
    }

    public List<Credential> getCredentials() {
        return Collections.unmodifiableList(credentials);
    }

    public boolean isLongTermAuthenticationRequest() {
        return this.longTermAuthenticationRequest;
    }
}