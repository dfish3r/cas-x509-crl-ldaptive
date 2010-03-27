package org.jasig.cas.server.authentication;

import java.util.Date;
import java.util.List;

/**
 * Represents a request to authenticate a user based on the provided credentials.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 */
public interface AuthenticationRequest {

    /**
     * The date/time the authentication request was created.  CANNOT be null.  MUST be immutable.
     *
     * @return the date/time the request was created.
     */
    Date getAuthenticationRequestDate();

    /**
     * The list of provided credentials.  CANNOT be NULL.  Should NOT be empty.
     *
     * @return the list of credentials.
     */
    List<Credential> getCredentials();

    /**
     * Whether the user requested a long term authentication request or not.  This is commonly known as "Remember Me."
     *
     * @return true if it did, false otherwise.
     */
    boolean isLongTermAuthenticationRequest();

}
