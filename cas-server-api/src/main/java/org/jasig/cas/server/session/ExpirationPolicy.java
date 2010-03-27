package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;

/**
 * Defines a policy for determining whether an object is expired or not.  Assumes that these objects
 * hold some form of {@link org.jasig.cas.server.session.State}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 */
public interface ExpirationPolicy {

    /**
     * Determines whether the object is valid or not.
     *
     * @param state the state to check.
     * @param authentication the root authentication object, if it exists.  Implementations should check if this is null
     * if they plan on using it.
     * @return true if its expired, false otherwise.
     */
    boolean isExpired(State state, Authentication authentication);
}
