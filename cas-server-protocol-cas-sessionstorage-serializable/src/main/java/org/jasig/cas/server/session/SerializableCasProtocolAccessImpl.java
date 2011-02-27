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

import org.jasig.cas.server.CasProtocolVersion;
import org.jasig.cas.server.login.CasServiceAccessRequestImpl;
import org.jasig.cas.server.login.ServiceAccessRequest;

/**
 * Represents a CAS service that can be stored in any of the storage mechanisms that can handle serialization.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class SerializableCasProtocolAccessImpl extends AbstractStaticCasProtocolAccessImpl implements SerializableSessionAware {

    private ValidationStatus validationStatus = ValidationStatus.NOT_VALIDATED;

    private final State state = new SimpleStateImpl();

    private transient Session parentSession;

    private final boolean renewed;

    private final boolean postRequest;

    private CasProtocolVersion casProtocolVersion;

    private boolean localSessionDestroyed = false;

    private final String resourceIdentifier;

    private final String id;

    private String delegatedSessionIdentifier;

    public SerializableCasProtocolAccessImpl(final Session session, final ServiceAccessRequest serviceAccessRequest) {
        this.parentSession = session;
        this.renewed = serviceAccessRequest.isForceAuthentication() || session.getAccesses().size() == 1;
        this.resourceIdentifier = serviceAccessRequest.getServiceId();
        this.postRequest = (serviceAccessRequest instanceof CasServiceAccessRequestImpl) && ((CasServiceAccessRequestImpl) serviceAccessRequest).isPostRequest();
        this.id = createId();

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
    protected State getState() {
        return this.state;
    }

    @Override
    protected boolean isRenewed() {
        return this.renewed;
    }

    @Override
    protected boolean isPostRequest() {
        return this.postRequest;
    }

    @Override
    protected CasProtocolVersion getCasVersion() {
        return this.casProtocolVersion;
    }

    @Override
    protected void setCasProtocolVersion(final CasProtocolVersion casVersion) {
        this.casProtocolVersion = casVersion;
    }

    @Override
    protected Session getParentSession() {
        return this.parentSession;
    }

    @Override
    protected void setLocalSessionDestroyed(final boolean localSessionDestroyed) {
        this.localSessionDestroyed = localSessionDestroyed;
    }

    @Override
    protected void setDelegatedSessionIdentifier(final String delegatedSessionIdentifier) {
        this.delegatedSessionIdentifier = delegatedSessionIdentifier;
    }

    @Override
    protected String getDelegatedSessionIdentifier() {
        return this.delegatedSessionIdentifier;
    }

    public String getId() {
        return this.id;
    }

    public String getResourceIdentifier() {
        return this.resourceIdentifier;
    }

    public boolean isLocalSessionDestroyed() {
        return this.localSessionDestroyed;
    }

    public void setSession(final Session session) {
        this.parentSession = session;
    }
}
