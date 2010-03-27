package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.authentication.JpaAuthenticationImpl;
import org.springframework.util.Assert;

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

    @Embedded
    private JpaAuthenticationImpl authentication;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentSession", fetch = FetchType.EAGER, targetEntity = JpaSessionImpl.class)
    private Set<Session> childSessions = new HashSet<Session>();

    @ManyToOne(optional=true)
    @JoinColumn(name="parent_session_id")
    private JpaSessionImpl parentSession;

//    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentSession", fetch = FetchType.EAGER)
//    private Set<JpaCasProtocolAccessImpl> casProtocolAccesses = new HashSet<JpaCasProtocolAccessImpl>();

    public JpaSessionImpl() {
        // this is for JPA
    }
   public JpaSessionImpl(final Authentication authentication) {
        this(null, authentication);
    }

    public JpaSessionImpl(final Session parentSession, final Authentication authentication) {
        this.parentSession = (JpaSessionImpl) parentSession;
        this.authentication = (JpaAuthenticationImpl) authentication;
        updateId();
    }

    protected void updateState() {
        this.state.updateState();
    }

    protected boolean executeExpirationPolicy() {
        return getExpirationPolicy().isExpired(this.state, this.authentication);
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

    public Authentication getAuthentication() {
        return this.authentication;
    }

    public void updateAuthentication(Authentication authentication) {
        Assert.isInstanceOf(JpaAuthenticationImpl.class, authentication);
        this.authentication = (JpaAuthenticationImpl) authentication;
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

    protected Session createDelegatedSessionInternal(final Authentication authentication) {
        final Session session = new JpaSessionImpl(this, authentication);
        this.childSessions.add(session);
        return session;

    }
}
