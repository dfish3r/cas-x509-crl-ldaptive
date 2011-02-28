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

import org.hibernate.annotations.Index;
import org.jasig.cas.server.CasProtocolVersion;
import org.jasig.cas.server.login.CasServiceAccessRequestImpl;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.springframework.util.Assert;

import javax.persistence.*;

/**
 * JPA-backed implementation of the {@link org.jasig.cas.server.session.Access} representing the CAS Protocol.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@Entity(name="casProtocolAccess")
@Table(name="cas_protocol_access")
@org.hibernate.annotations.Table(appliesTo = "cas_protocol_access", indexes = {@Index(name="index_cas_protocol_id", columnNames = "id")})
public class JpaCasProtocolAccessImpl extends AbstractStaticCasProtocolAccessImpl {

    @Id
    @Column(name="id")
    private String id;

    @Column(name="renew", insertable = true, updatable = true)
    private boolean renewed;

    @Embedded
    private JpaStateImpl state = new JpaStateImpl();

    @Enumerated(EnumType.STRING)
    @Column(name="validation_status", insertable = true, updatable = true, nullable = false)
    private ValidationStatus validationStatus = ValidationStatus.NOT_VALIDATED;

    @Enumerated(EnumType.STRING)
    @Column(name="cas_protocol_version", insertable = true, updatable = true, nullable = true)
    private CasProtocolVersion casVersion = CasProtocolVersion.UNDEFINED;

    @ManyToOne(optional = false)
    @JoinColumn(name="parent_session_id", nullable = false)
    private JpaSessionImpl parentSession;

    @Column(name="post_request", insertable = true, updatable = true)
    private boolean postRequest;

    @Column(name="resource_identifier", insertable = true, updatable = true, nullable = false)
    private String resourceIdentifier;

    @Column(name="local_session_destroyed",insertable = true, updatable = true)
    private boolean localSessionDestroyed;

    @Column(name="delegated_session_identifier", insertable = true, updatable = true, length = 255)
    private String delegatedSessionIdentifier;

    public JpaCasProtocolAccessImpl() {
        // this is for JPA
    }

    public JpaCasProtocolAccessImpl(final Session session, final ServiceAccessRequest request) {
        Assert.isInstanceOf(JpaSessionImpl.class, session);
        this.parentSession = (JpaSessionImpl) session;
        this.renewed = request.isForceAuthentication() || session.getAccesses().isEmpty();
        this.resourceIdentifier = request.getServiceId();
        this.id = createId();
        this.postRequest = (request instanceof CasServiceAccessRequestImpl) && ((CasServiceAccessRequestImpl) request).isPostRequest();
    }

    public Session getSession() {
        return this.parentSession;
    }

    protected final ValidationStatus getValidationStatus() {
        return this.validationStatus;
    }

    protected final void setValidationStatus(final ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    protected final State getState() {
        return this.state;
    }

    protected final boolean isRenewed() {
        return this.renewed;
    }

    protected final boolean isPostRequest() {
        return this.postRequest;
    }

    protected final CasProtocolVersion getCasVersion() {
        return this.casVersion;
    }

    protected final void setCasProtocolVersion(final CasProtocolVersion casVersion) {
        this.casVersion = casVersion;
    }

    public String getId() {
        return this.id;
    }

    public String getResourceIdentifier() {
        return this.resourceIdentifier;
    }

    public final Session getParentSession() {
        return this.parentSession;
    }

    public final boolean isLocalSessionDestroyed() {
        return this.localSessionDestroyed;
    }

    @Override
    protected final void setLocalSessionDestroyed(final boolean localSessionDestroyed) {
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
}
