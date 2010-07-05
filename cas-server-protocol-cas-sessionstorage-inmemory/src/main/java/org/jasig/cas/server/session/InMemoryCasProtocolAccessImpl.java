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
import org.jasig.cas.server.util.ServiceIdentifierMatcher;
import org.jasig.cas.server.util.UniqueTicketIdGenerator;

/**
 * Concrete, in-memory aware, implementation of the {@link org.jasig.cas.server.session.AbstractCasProtocolAccessImpl}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 */
public final class InMemoryCasProtocolAccessImpl extends AbstractCasProtocolAccessImpl {

    private final State state = new SimpleStateImpl();

    private final Session parentSession;

    private final String id;

    private final boolean renewed;

    private final String resourceIdentifier;

    private final ServiceIdentifierMatcher serviceIdentifierMatcher;

    private final boolean post;

    private final ProxyHandler proxyHandler;

    private final ExpirationPolicy expirationPolicy;

    private CasProtocolVersion casVersion;

    private boolean localSessionDestroyed;

    private ValidationStatus validationStatus = ValidationStatus.NOT_VALIDATED;

    private final UniqueTicketIdGenerator uniqueTicketIdGenerator;

    public InMemoryCasProtocolAccessImpl(final Session session, final ServiceAccessRequest request, final ServiceIdentifierMatcher serviceIdentifierMatcher, final ProxyHandler proxyHandler, final ExpirationPolicy expirationPolicy, final UniqueTicketIdGenerator uniqueTicketIdGenerator) {
        this.parentSession = session;
        this.renewed = request.isForceAuthentication() || session.getAccesses().size() == 1;
        this.resourceIdentifier = request.getServiceId();
        this.id = createId();
        this.serviceIdentifierMatcher = serviceIdentifierMatcher;
        this.post = (request instanceof CasServiceAccessRequestImpl) && ((CasServiceAccessRequestImpl) request).isPostRequest();
        this.proxyHandler = proxyHandler;
        this.expirationPolicy = expirationPolicy;
        this.uniqueTicketIdGenerator = uniqueTicketIdGenerator;
    }

    public final String getId() {
        return this.id;
    }

    public final String getResourceIdentifier() {
        return this.resourceIdentifier;
    }

    protected final void setValidationStatus(final ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    public final Session getParentSession() {
        return this.parentSession;
    }

    protected final ValidationStatus getValidationStatus() {
        return this.validationStatus;
    }

    protected final State getState() {
        return this.state;
    }

    protected boolean isRenewed() {
        return this.renewed;
    }

    protected ServiceIdentifierMatcher getServiceIdentifierMatcher() {
        return this.serviceIdentifierMatcher;
    }

    protected boolean isPostRequest() {
        return this.post;
    }

    protected CasProtocolVersion getCasVersion() {
        return this.casVersion;
    }

    protected void setCasProtocolVersion(final CasProtocolVersion casVersion) {
        this.casVersion = casVersion;
    }

    protected ProxyHandler getProxyHandler() {
        return this.proxyHandler;
    }

    @Override
    protected ExpirationPolicy getExpirationPolicy() {
        return this.expirationPolicy;
    }

    @Override
    protected UniqueTicketIdGenerator getIdGenerator() {
        return this.uniqueTicketIdGenerator;
    }

    public boolean isLocalSessionDestroyed() {
        return this.localSessionDestroyed;
    }

    @Override
    protected void setLocalSessionDestroyed(final boolean localSessionDestroyed) {
        this.localSessionDestroyed = localSessionDestroyed;
    }
}
