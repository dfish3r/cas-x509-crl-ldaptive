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

import org.jasig.cas.server.Saml11Profile;
import org.jasig.cas.server.authentication.AuthenticationResponse;

/**
 * In-memory implementation of the SAML protocol that supports in-memory storage.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class InMemorySaml11ProtocolAccessImpl extends AbstractSaml1ProtocolAccessImpl {

    private ValidationStatus validationStatus = ValidationStatus.NOT_VALIDATED;

    private final Saml11Profile profile;

    private final Session session;

    private final String resourceIdentifier;

    private final long issueLength;

    private final String issuer;

    private String requestId;

    private final String id;

    public InMemorySaml11ProtocolAccessImpl(final Saml11Profile profile, final String id, final String resourceIdentifier, final String issuer, final long issueLength, final Session parentSession) {
        this.id = id;
        this.resourceIdentifier = resourceIdentifier;
        this.issuer = issuer;
        this.issueLength = issueLength;
        this.session = parentSession;
        this.profile = profile;
    }

    @Override
    protected String getRequestId() {
        return this.requestId;
    }

    @Override
    protected void setRequestId(final String requestId) {
        this.requestId = requestId;
    }

    @Override
    protected ValidationStatus getValidationStatus() {
        return this.validationStatus;
    }

    @Override
    protected void setValidationStatus(final ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    @Override
    protected Saml11Profile getProfile() {
        return this.profile;
    }

    @Override
    protected Session getParentSession() {
        return this.session;
    }

    @Override
    protected String getIssuer() {
        return this.issuer;
    }

    @Override
    protected long getIssueLength() {
        return this.issueLength;
    }

    public Session createDelegatedSession(final AuthenticationResponse authenticationResponse) throws InvalidatedSessionException {
        throw new UnsupportedOperationException();
    }

    public String getResourceIdentifier() {
        return this.resourceIdentifier;
    }

    public String getId() {
        return this.id;
    }
}
