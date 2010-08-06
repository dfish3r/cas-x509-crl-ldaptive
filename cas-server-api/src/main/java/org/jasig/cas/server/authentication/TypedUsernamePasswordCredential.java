package org.jasig.cas.server.authentication;

/**
 * Extension to the {@link org.jasig.cas.server.authentication.UserNamePasswordCredential} that allows a user or the
 * system to denote the type of credential that is being used.
 * <p>
 * Designed to work with the {@link AuthenticationHandler#getName()} method.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface TypedUsernamePasswordCredential extends UserNamePasswordCredential {

    /**
     * Matches the type of the credential provided.  Can be null if we don't know the specific type of credential.
     *
     * @return the type of credential or null if we're not sure.
     */
    String getType();
}
