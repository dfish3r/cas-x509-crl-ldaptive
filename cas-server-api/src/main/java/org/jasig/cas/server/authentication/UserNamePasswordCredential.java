package org.jasig.cas.server.authentication;

/**
 * Represents the traditional user name/password combination that the majority of users would provide as
 * a form of credentials.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface UserNamePasswordCredential extends Credential {

    /**
     * Retrieves the user name that was provided.
     *
     * @return the user name.  This should NEVER be null.
     */
    String getUserName();

    /**
     * Retrieves the password that was provided.
     *
     * @return the password.  This should NEVER be null.
     */
    String getPassword();
}
