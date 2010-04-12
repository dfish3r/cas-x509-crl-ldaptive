package org.jasig.cas.server.authentication;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class PlainTextPasswordEncoder implements PasswordEncoder {

    public boolean isValidPassword(final String encodedPassword, final String providedPassword, final Object salt) {
        return encodedPassword != null && providedPassword != null && encodedPassword.equals(providedPassword);
    }
}
