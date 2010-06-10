package org.jasig.cas.server.authentication;

import java.util.List;

/**
 * Resolves the messages for a given credential.  These are not per principal because its more likely that a given
 * credential has warnings or messages versus a principal.  Though these should be able to still find them.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 */
public interface MessageResolver {

    /**
     * Resolves messages for a given credential.  This should only execute if the authentication was successful.
     * <p>
     * GeneralSecurityExceptions should be used to relay messages when authentication failed.
     *
     * @param credential the credentials.  Cannot be null.
     * @param authenticationHandler the authentication handler.  Cannot be null.
     * @return the messages.  CANNOT be NULL.  But can be empty.
     */
    List<Message> resolveMessagesFor(Credential credential, AuthenticationHandler authenticationHandler);
}
