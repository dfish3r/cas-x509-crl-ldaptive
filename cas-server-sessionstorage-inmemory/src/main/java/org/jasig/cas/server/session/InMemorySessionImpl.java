package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Default in-memory implementation of the Sessions.  Since objects are stored in local memory only references
 * to factories, etc. can be maintained within the application.  Sessions that might be stored in databases, etc, would
 * not necessarily preserve the links during storage, but may instead re-establish the link once the object is
 * retrieved from its data store.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class InMemorySessionImpl extends AbstractSession {

    private final Session parentSession;

    private final ExpirationPolicy expirationPolicy;

    private String id;

    private boolean invalidate = false;

    private final Map<String, Access> accesses = new ConcurrentHashMap<String,Access>();

    private final List<AccessFactory> accessFactories;

    private Authentication authentication;

    private final State state = new SimpleStateImpl();

    private final Set<Session> childSessions = new HashSet<Session>();

    public InMemorySessionImpl(final ExpirationPolicy expirationPolicy, final List<AccessFactory> accessFactories, final Authentication authentication) {
        this(null, expirationPolicy, accessFactories, authentication);
    }

    public InMemorySessionImpl(final Session parentSession, final ExpirationPolicy expirationPolicy, final List<AccessFactory> accessFactories, final Authentication authentication) {
        this.parentSession = parentSession;
        this.expirationPolicy = expirationPolicy;
        this.accessFactories = accessFactories;
        this.authentication = authentication;
        updateId();
    }

    protected Set<Session> getChildSessions() {
        return this.childSessions;
    }

    protected Session getParentSession() {
        return this.parentSession;
    }

    protected boolean executeExpirationPolicy() {
        return this.expirationPolicy.isExpired(state, getRootAuthentication());
    }

    protected boolean isInvalid() {
        return this.invalidate;
    }

    protected void updateId() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return this.id;
    }

    public void updateAuthentication(final Authentication authentication) {
        this.authentication = authentication;
    }

    public Authentication getAuthentication() {
        return this.authentication;
    }

    protected void setInvalidFlag() {
        this.invalidate = true;
    }

    public Access getAccess(String accessId) {
        return this.accesses.get(accessId);
    }

    public Collection<Access> getAccesses() {
        return this.accesses.values();
    }

    protected List<AccessFactory> getAccessFactories() {
        return this.accessFactories;
    }

    protected void addAccess(final Access access) {
        this.accesses.put(access.getId(), access);
    }

    protected void updateState() {
        this.state.updateState();
    }

    protected Session createDelegatedSessionInternal(final Authentication authentication) {
        final Session session = new InMemorySessionImpl(this, this.expirationPolicy, this.accessFactories, authentication);
        this.childSessions.add(session);
        return session;
    }
}
