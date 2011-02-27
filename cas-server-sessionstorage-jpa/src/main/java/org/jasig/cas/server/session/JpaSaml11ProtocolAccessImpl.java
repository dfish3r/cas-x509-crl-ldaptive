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
import org.jasig.cas.server.Saml11Profile;
import org.jasig.cas.server.login.Saml11RequestAccessRequestImpl;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.springframework.util.Assert;

import javax.persistence.*;

/**
 * JPA-backed implementation of the SAML 1.1 protocol.
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 *
 */
@Entity(name="saml11ProtocolAccess")
@Table(name="saml11_protocol_access")
@org.hibernate.annotations.Table(appliesTo = "saml11_protocol_access", indexes = {@Index(name="index_saml11_protocol_id", columnNames = "id")})
public class JpaSaml11ProtocolAccessImpl extends AbstractStaticSaml1ProtocolAccessImpl {

    @Column(name="resource_identifier", insertable = true, updatable = true, nullable = false)
    private String resourceIdentifier;

    @Id
    @Column(name="id")
    private String id;

    @Embedded
    private JpaStateImpl state = new JpaStateImpl();

    @Column(name="request_id", insertable = true, updatable = true, nullable = true)
    private String requestId;

    @Column(name="issue_length", insertable = true, updatable = true, nullable = false)
    private long issueLength;

    @Column(name="issuer", insertable = true, updatable = true, nullable = false)
    private String issuer;

    @ManyToOne(optional = false)
    @JoinColumn(name="parent_session_id", nullable = false)
    private JpaSessionImpl parentSession;

    @Enumerated(EnumType.STRING)
    @Column(name="saml_profile", insertable = true, updatable = true, nullable = false)
    private Saml11Profile profile;

    @Enumerated(EnumType.STRING)
    @Column(name="validation_status", insertable = true, updatable = true, nullable = false)
    private ValidationStatus validationStatus;

    public JpaSaml11ProtocolAccessImpl() {
        // this is only for JPA.
    }

    public JpaSaml11ProtocolAccessImpl(final String id, final Session session, final ServiceAccessRequest request, final String issuer, final long issueLength) {
        Assert.isInstanceOf(JpaSessionImpl.class, session);
        this.parentSession = (JpaSessionImpl) session;
        this.resourceIdentifier = request.getServiceId();

        final Saml11RequestAccessRequestImpl saml11RequestAccessRequest = (Saml11RequestAccessRequestImpl) request;
        this.profile = saml11RequestAccessRequest.getProfile();
        this.issuer = issuer;
        this.issueLength = issueLength;
        this.id = id;
        this.validationStatus = ValidationStatus.NOT_VALIDATED;
    }

    public String getResourceIdentifier() {
        return this.resourceIdentifier;
    }

    public String getId() {
        return this.id;
    }

    @Override
    protected State getState() {
        return this.state;
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
    protected long getIssueLength() {
        return this.issueLength;
    }

    @Override
    protected String getIssuer() {
        return this.issuer;
    }

    @Override
    protected Session getParentSession() {
        return this.parentSession;
    }

    @Override
    protected Saml11Profile getProfile() {
        return this.profile;
    }

    @Override
    protected void setValidationStatus(final ValidationStatus validationStatus) {
        this.validationStatus = validationStatus;
    }

    @Override
    protected ValidationStatus getValidationStatus() {
        return this.validationStatus;
    }
}
