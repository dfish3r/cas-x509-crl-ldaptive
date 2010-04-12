package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.login.ServiceAccessRequest;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * A session is the transient item stored within the CAS server to represent a short-lived authentication.  From this,
 * without holding on to existing credentials, users can gain access to other services and resources.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */

public interface Session extends Serializable {

    /**
     * Returns the String identifier for this session.
     *
     * @return the String identifier for this session. CANNOT be null.
     */
    String getId();

    /**
     * The authentication associated with this session explicitly.  CANNOT be null.
     *
     * @return the authentication associated with this session explicitly.
     */
    Set<Authentication> getAuthentications();

    /**
     * The ROOT authentication, at the top of the tree.  If this is the top session, then calling getAuthentication
     * and this method have the same effect.
     * <p>
     * CANNOT be null.
     *
     * @return the ROOT authentication at the top of the tree.
     */
    Set<Authentication> getRootAuthentications();

    /**
     * Returns the ROOT session, the one at the top of the list.  If this is the top session, then calling getRootSession()
     * returns itself.
     *
     * @return returns the root session.  CANNOT be NULL.
     */
    Session getRootSession();

    /**
     * Updates the authentication associated with the session.
     *
     * @param authentication the authentication to update with.  CANNOT be NULL.
     */
    void addAuthentication(Authentication authentication);

    /**
     * Enumerates the list of authentications beyond the original, in reverse order.  So the newest one is that the top.
     * This list can be empty but CANNOT be null.
     *
     * @return the list of authentications beyond the original / ROOT.
     */
    List<Set<Authentication>> getProxiedAuthentications();

    /**
     * Invalidates the current session if its not invalid yet.  This fails silently (as in calling invalidate on an
     * invalidated session will not do anything.
     * <p>
     * This should also invalidate any sessions downstream (though not upstream).
     *
     * @return the list of accesses associated with this invalidation request.
     */
    Set<Access> invalidate();

    /**
     * Attempts to grant access to the service.
     *
     * @param serviceAccessRequest the service to grant access to.
     *
     * @return the Access to be granted.  Can be null if access was not granted.
     * @throws InvalidatedSessionException when a session is invalidated but you try to use it.
     *
     */
    Access grant(ServiceAccessRequest serviceAccessRequest) throws InvalidatedSessionException;

    /**
     * Retrieve the access request from the appropriate session.
     *
     * @param accessId the access id to retrieve.
     * @return the access.  Can BE null if the id does not match one.
     */
    Access getAccess(String accessId);

    /**
     * Retrieves the list of accesses associated with a Session.  The list can be empty but NOT null.
     * <p>
     * This list has to return any Access that is marked as "can logout" or "requires storage" but is not required to store
     * sites where you can not log out of or that don't require storage.  Its is RECOMMENDED that the return include those.
     *
     * @return the list of accesses associated with a Session.
     */
    Collection<Access> getAccesses();

    /**
     * Returns whether the current session is valid or not.
     *
     * @return true if it is valid, false otherwise.
     */
    boolean isValid();

    /**
     * Determines whether this is the ROOT authentication or not.
     * @return true if it is, false otherwise.
     */
    boolean isRoot();

    /**
     * Associates a child session with this session.  A child session is generally one that depends on some aspect of
     * another session (generally the original session was used to authenticate to create the child one).
     *
     * @param authentication the new authentication.
     * @throws InvalidatedSessionException when a session is invalidated but you try to use it.
     * @return the newly created child session.
     */
    Session createDelegatedSession(Authentication authentication) throws InvalidatedSessionException;

    /**
     * Locates a child session *if* you know its original identifier.
     *
     * @param identifier the identifier, cannot be null.
     * @return the session if its found, null otherwise.
     */
    Session findChildSessionById(final String identifier);

    /**
     * Determines whether the session has been used to grant an access already.  The equivalent of saying getAccesses().
     * isEmpty().
     *
     * @return true if it has not been used.  False otherwise.
     */
    boolean hasNotBeenUsed();
}