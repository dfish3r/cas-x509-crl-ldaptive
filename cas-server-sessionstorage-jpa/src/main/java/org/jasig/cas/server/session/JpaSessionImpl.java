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

import org.jasig.cas.server.authentication.*;

import javax.persistence.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * JPA-backed implementation of the {@link org.jasig.cas.server.session.Session}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 * // TODO enable access
 */
@Entity(name="session")
@Table(name="session")
public final class JpaSessionImpl extends AbstractStaticSession {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "cas_session_seq")
    @SequenceGenerator(name="cas_session_seq",sequenceName="cas_session_seq",initialValue=1,allocationSize=50)
    private long id;

    @Column(name="sessionId")
    private String sessionId;

    @Embedded
    private JpaStateImpl state = new JpaStateImpl();

    @Column(name="session_invalid",updatable = true, insertable = true)
    private boolean invalid = false;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentSession", fetch = FetchType.EAGER, targetEntity = JpaSessionImpl.class)
    private Set<Session> childSessions = new HashSet<Session>();

    @ManyToOne(optional=true)
    @JoinColumn(name="parent_session_id")
    private JpaSessionImpl parentSession;

    @Embedded
    private JpaAttributePrincipalImpl attributePrincipal;

    @OneToMany(cascade = CascadeType.ALL,orphanRemoval = true,targetEntity = JpaAuthenticationImpl.class)
    private Set<Authentication> authentications = new HashSet<Authentication>();

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentSession", fetch = FetchType.EAGER)
//    private Set<JpaCasProtocolAccessImpl> casProtocolAccesses = new HashSet<JpaCasProtocolAccessImpl>();

    public JpaSessionImpl() {
        // this is for JPA
    }
   public JpaSessionImpl(final AuthenticationResponse authenticationResponse) {
        this(null, authenticationResponse);
    }

    public JpaSessionImpl(final Session parentSession, final AuthenticationResponse authenticationResponse) {
        this.parentSession = (JpaSessionImpl) parentSession;
        this.authentications.addAll(authenticationResponse.getAuthentications());
        this.attributePrincipal = (JpaAttributePrincipalImpl) authenticationResponse.getPrincipal();
        updateId();

        for (final Authentication authentication : this.authentications) {
            if (authentication.isLongTermAuthentication()) {
                this.state.setLongTermAuthentication(true);
                break;
            }
        }
    }

    protected void updateState() {
        this.state.updateState();
    }

    protected boolean executeExpirationPolicy() {
        return getExpirationPolicy().isExpired(this.state);
    }

    protected boolean isInvalid() {
        return this.invalid;
    }

    protected Session getParentSession() {
        return this.parentSession;
    }

    protected void updateId() {
        this.sessionId = UUID.randomUUID().toString();
    }

    protected void addAccess(final Access access) {
//        if (access instanceof JpaCasProtocolAccessImpl) {
//            this.casProtocolAccesses.add((JpaCasProtocolAccessImpl) access);
//        }
    }

    protected Set<Session> getChildSessions() {
        return this.childSessions;
    }

    protected void setInvalidFlag() {
        this.invalid = true;
    }

    public String getId() {
        return this.sessionId;
    }

    public Access getAccess(final String accessId) {
//        for (final Access access : this.casProtocolAccesses) {
//           if (access.getId().equals(accessId)) {
//                return access;
//            }
//        }

        return null;
    }

    public Collection<Access> getAccesses() {
        final Set<Access> accesses = new HashSet<Access>();

 //       accesses.addAll(this.casProtocolAccesses);

        return accesses;
    }

    @Override
    protected Session createDelegatedSessionInternal(final AuthenticationResponse authenticationResponse) {
        final Session session = new JpaSessionImpl(this, authenticationResponse);
        this.childSessions.add(session);
        return session;
    }

    public Set<Authentication> getAuthentications() {
        return this.authentications;
    }

    public AttributePrincipal getPrincipal() {
        return this.attributePrincipal;
    }

    public void addAuthentication(final Authentication authentication) {
        if (authentication.isLongTermAuthentication()) {
            this.state.setLongTermAuthentication(true);
        }

        this.authentications.add(authentication);
    }
}
