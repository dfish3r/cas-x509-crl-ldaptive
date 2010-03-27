package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.login.ServiceAccessRequest;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Implements the underlying domain object methods that should be common among every specific implementation of the
 * session interface.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSession implements Session {

    /**
     * Retrieve the expiration policy, which defines whether this session is still valid or not.
     *
     * @return the result of {@link org.jasig.cas.server.session.ExpirationPolicy#isExpired(State, Authentication)}
     */
    protected abstract boolean executeExpirationPolicy();

    /**
     * Determines whether this session has been marked as invalid or not (regardless of expiration policy)
     *
     * @return true if the session has been marked as invalid, false otherwise.
     */

    protected abstract boolean isInvalid();

    /**
     * Returns the parent session to this session, if they are linked.  CAN be null.
     *
     * @return the parent session or null if there is none.
     */
    protected abstract Session getParentSession();

    /**
     * Called after certain actions to regenerate the identifier for this session.  Things that store links to the session
     * should be aware that the identifier can change.  Note, however, that updating identifiers is NOT required.
     */
    protected abstract void updateId();

    /**
     * Retrieves the list of AccessFactories.
     *
     * @return the list of access factories.  CANNOT be null.  SHOULD NOT be empty.
     */
    protected abstract List<AccessFactory> getAccessFactories();

    /**
     * Store the Access created in the meta data for the
     *
     * @param access the access to store.
     */
    protected abstract void addAccess(Access access);

    /**
     * Retrieves the child sessions.
     *
     * @return the child sessions.  CANNOT be NULL.  Can be EMPTY.
     */
    protected abstract Set<Session> getChildSessions();

    /**
     * Sets the invalid flag on a subclass.
     */
    protected abstract void setInvalidFlag();

    /**
     * Updates the internal state of a subclass.
     */
    protected abstract void updateState();

    public synchronized final Set<Access> invalidate() {
        final Set<Access> accesses = new HashSet<Access>();
        accesses.addAll(getAccesses());

        for (final Access access : getAccesses()) {
            access.invalidate();
        }

        for (final Session session : getChildSessions()) {
            accesses.addAll(session.invalidate());
        }

        setInvalidFlag();
        return accesses;
    }

    public final boolean isValid() {
        return !isInvalid() && !executeExpirationPolicy() && (getParentSession() == null || getParentSession().isValid());
    }

    public final Authentication getRootAuthentication() {
        if (getParentSession() != null) {
            return getParentSession().getRootAuthentication();
        }

        return getAuthentication();
    }

    public final Session getRootSession() {
        if (getParentSession() != null) {
            return getParentSession().getRootSession();
        }

        return this;
    }

    public synchronized final Access grant(final ServiceAccessRequest serviceAccessRequest) {
        if (!isValid()) {
            throw new IllegalStateException("Session is no longer valid.");
        }

        updateState();

        for (final AccessFactory accessFactory : getAccessFactories()) {
            final Access access = accessFactory.getAccess(this, serviceAccessRequest);

            if (access != null) {
                addAccess(access);
                // TODO re-enable later when we want to change session ids
                // updateId();
                return access;
            }
        }

        throw new IllegalStateException("No AccessFactories configured that can execute Access request.");
    }

    public final List<Authentication> getProxiedAuthentications() {
        if (getParentSession() == null) {
            return new ArrayList<Authentication>();
        }

        final List<Authentication> authentications = getParentSession().getProxiedAuthentications();
        authentications.add(getAuthentication());
        return authentications;
    }

    public final boolean isRoot() {
        return getParentSession() == null;
    }

    public final Session findChildSessionById(final String identifier) {
        for (final Session session : getChildSessions()) {
            if (session.getId().equals(identifier)) {
                return session;
            }
        }
        return null;
    }

    public synchronized final Session createDelegatedSession(Authentication authentication) {
        updateState();
        return createDelegatedSessionInternal(authentication);
    }

    protected abstract Session createDelegatedSessionInternal(Authentication authentication);
}
