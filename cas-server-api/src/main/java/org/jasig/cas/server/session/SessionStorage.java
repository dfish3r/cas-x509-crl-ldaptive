package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.login.LoginRequest;

import java.util.Set;

/**
 * Represents a storage area for session information.  The backing mechanism can be any mechanism that
 * can store Java objects or a representation of those Java objects, such as in-memory, database, etc.
 * Because the underlying-specifics of the Sessions may be dependent on the backing mechanism, the
 * SessionStorage implementation is responsible for creating sessions.
 * <p>
 * Classes that rely on the SessionStorage should never assume that updates are automatically persisted
 * and should take care to call updateSession after making changes to the underlying session.
 * <p>
 * Implementations of this class MUST be thread-safe.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface SessionStorage {

    /**
     * Constructs a new Session from an existing AuthenticationRequest.
     * <p>
     * Note: In CAS3, these were called TicketGrantingTickets. We have disambiguated from that term because we are
     * supporting more than one protocol going forward.
     *
     * @param loginRequest the initial login request
     * @param authentication the successful authentication that will be used to populate this session.
     * @return the fully constructed session.  This should never return null.
     * @throws InvalidatedSessionException if we try to create a session from an expired session.
     */
    Session createSession(LoginRequest loginRequest, Authentication authentication) throws InvalidatedSessionException;

    /**
     * Destroys an existing session, which means that it erases it from whatever backing storage
     * the implementation is using.
     * <p>
     * Method returns the Session in case the calling class has any last minute need for the Session.  Calling
     * classes *should* invalidate the session if they have not done so.  This method only removes the internal references
     * to the object.
     *
     * @param sessionId the session identifier to look for.
     * @return the Session. May return null if it can't find it.
     */
    Session destroySession(String sessionId);

    /**
     * Locates an existing session based on its existing session identifier.
     *
     * @param sessionId the id of the session to look for.
     * @return the Session, if found.  Null, otherwise.
     */
    Session findSessionBySessionId(String sessionId);

    /**
     * Updates the existing session in the backing-storage mechanism.  Depending on the implementation
     * this may or may not actually do anything.  One could imagine an in-memory version not needing to
     * do anything explicit to save a session back as all operations take place in-memory.
     *
     * @param session the session to update in the backing data-store.
     * @return the session that was updated.
     */
    Session updateSession(Session session);

    /**
     * Locate a session based on the access request identifier.
     * <p>
     * Note: In CAS3, ServiceTickets take the place of Access.
     *
     * @param accessId the identifier for which to retrieve the service by.
     *
     * @return the session associated with the ServiceAccessId, or null if the match can't be found.
     */
    Session findSessionByAccessId(String accessId);

    /**
     * Returns the set of sessions associated with this particular principal.
     *
     * @param principalName the principal name.  CANNOT be NULL.
     * @return the set of sessions associated with that principal.  CANNOT be NULL.  Can be empty.
     */
    Set<Session> findSessionsByPrincipal(String principalName);

    /**
     * Clears out the entire backing storage.  Doing this is not recommended, and may not be implemented by all back-end
     * storage mechanisms.
     */
    void purge();
}