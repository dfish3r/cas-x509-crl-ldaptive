package org.jasig.cas.server.authentication;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;

/**
 * Represents a response to an authentication request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 */
public interface AuthenticationResponse {

    /**
     * Notes whether the authentication attempt succeeded or not.
     *
     * @return true if it did, false otherwise.
     */
    boolean succeeded();

    /**
     * Returns information about the successful authentication.  This could include the principal, time, etc.
     *
     * If succeeded == true, then this CANNOT be null.
     *
     * @return the authentication, or null if authentication failed.
     */
    Authentication getAuthentication();

    /**
     * Contains the list of GeneralSecurityExceptions that may have occurred during the course of the Authentication.
     * <p>
     * This makes no assumptions about whether authentication succeeded or failed based on these exceptions, so one should
     * ALWAYS check succeeded.
     * <p>
     * This list can be empty but CANNOT be null.
     * @return the list of authentication exceptions.  CANNOT be null.
     */
    List<GeneralSecurityException> getGeneralSecurityExceptions();

    /**
     * Retrieve the list of Authentication messages, which may be things like "Your password expires in 2 days.".
     * This makes no assumptions about whether authentication succeeded or failed based on these exceptions, so one should
     * ALWAYS check succeeded.
     * <p>
     * This list can be empty but CANNOT be null.
     *
     * @return the list of authentication messages. CANNOT be null.
     */
    List<Message> getAuthenticationMessages();

    /**
     * A list of attributes related to the authentication response.  Examples can be CAPTCHA ids. This map is NOT immutable.
     * @return the map.  CAN be empty.  CANNOT be null.
     */
    Map<String, Object> getAttributes();
}
