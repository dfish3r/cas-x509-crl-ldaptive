package org.jasig.cas.server.authentication;

/**
 * Manages the authentication of the User's Credentials by processing the Authentication Request.  AuthenticationManagers
 * should be able to authenticate the entire AuthenticationRequest.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface AuthenticationManager {

    /**
     * This method executes the authentication check and produces an AuthenticationResponse.  This should not throw an
     * exception, but capture any underlying exceptions and consolidate them into the AuthenticationResponse.
     * <p>
     * The logic behind not throwing an exception, but consolidating them is CAS4 is that we can then take the messages
     * and send them back to the UI and provide a nice user experience to the user.
     *
     * @param authenticationRequest the request for authentication.
     * @return the response to the request.  This should NEVER be null.
     */
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
