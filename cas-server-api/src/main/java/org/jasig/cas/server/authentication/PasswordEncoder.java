package org.jasig.cas.server.authentication;

/**
 * Compares a supplied (providedPassword) password against an encoded password that may be stored in a database, etc.
 * using the (optional) supplied salt.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface PasswordEncoder {

    /**
     * Determines whether a password is valid or not.
     *
     * @param encodedPassword the encoded password from the authentication system.
     * @param providedPassword the password provided by the user.
     * @param salt the optional salt to use with the algorithm.
     * @return true if the password is valid, false otherwise.
     */
    boolean isValidPassword(String encodedPassword, String providedPassword, Object salt);
}
