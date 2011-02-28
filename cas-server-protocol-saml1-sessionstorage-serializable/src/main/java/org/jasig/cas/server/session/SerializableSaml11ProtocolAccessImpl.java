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
import org.jasig.cas.server.login.Saml11RequestAccessRequestImpl;

/**
 * Handles the creation of serializable SAML 1.1 objects.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class SerializableSaml11ProtocolAccessImpl extends AbstractStaticSaml1ProtocolAccessImpl implements SerializableSessionAware {

    private Session parentSession;

    private final State state = new SimpleStateImpl();

    private String requestId;

    private final String issuer;

    private final long issueLength;

    private ValidationStatus validationStatus = ValidationStatus.NOT_VALIDATED;

    private final Saml11Profile profile;

    private final String id;

    private final String resourceIdentifier;

    public SerializableSaml11ProtocolAccessImpl(final String id, final Session session, final Saml11RequestAccessRequestImpl saml11RequestAccessRequest, final String issuer, final long issueLength) {
        this.id = id;
        this.parentSession = session;
        this.profile = saml11RequestAccessRequest.getProfile();
        this.resourceIdentifier = saml11RequestAccessRequest.getServiceId();
        this.issuer = issuer;
        this.issueLength = issueLength;
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
        return this.parentSession;
    }

    @Override
    protected String getIssuer() {
        return this.issuer;
    }

    @Override
    protected long getIssueLength() {
        return this.issueLength;
    }

    @Override
    protected void setRequestId(final String requestId) {
        this.requestId = requestId;
    }

    @Override
    protected String getRequestId() {
        return this.requestId;
    }

    @Override
    protected State getState() {
        return this.state;
    }

    public String getId() {
        return this.id;
    }

    public String getResourceIdentifier() {
        return this.resourceIdentifier;
    }

    public void setSession(final Session session) {
        this.parentSession = session;
    }
}
