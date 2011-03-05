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

package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.login.TokenServiceAccessRequest;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class MockAccess implements Access, SerializableSessionAware {

    private final String id;

    private final String resourceIdentifier;

    private Session session;

    public MockAccess(final String id, final String resourceIdentifier) {
        this.id = id;
        this.resourceIdentifier = resourceIdentifier;
    }

    public String getId() {
        return this.id;
    }

    public String getResourceIdentifier() {
        return this.resourceIdentifier;
    }

    public void validate(TokenServiceAccessRequest tokenServiceAccessRequest) {

    }

    public boolean invalidate() {
        return false;
    }

    public boolean isLocalSessionDestroyed() {
        return false;
    }

    public boolean requiresStorage() {
        return true;
    }

    public boolean isUsed() {
        return false;
    }

    public Session createDelegatedSession(AuthenticationResponse authenticationResponse) throws InvalidatedSessionException {
        return null;
    }

    public AccessResponseResult generateResponse(AccessResponseRequest accessResponseRequest) {
        return null;
    }

    public void setSession(final Session session) {
        this.session = session;
    }
}
