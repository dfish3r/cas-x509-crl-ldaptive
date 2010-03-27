package org.jasig.cas.server.authentication;

/**
 * Takes the credential provided by the user, after they have been authenticated, and attempts to map them to a
 * Principal.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface CredentialToPrincipalResolver {

    /**
     * Resolves the provided credentials to a principal.
     *
     * @param credentials the credentials to attempt to resolve.
     * @return the principal that matches with the supplied credentials.  This CAN be null.
     */
    AttributePrincipal resolve(Credential credentials);

    /**
     * Determines whether the {@link org.jasig.cas.server.authentication.CredentialToPrincipalResolver} knows how
     * to process this type of credentials.
     *
     * @param credentials the credentials we want to know about
     * @return true if it can support them, false otherwise.
     */
    boolean supports(Credential credentials);
}
