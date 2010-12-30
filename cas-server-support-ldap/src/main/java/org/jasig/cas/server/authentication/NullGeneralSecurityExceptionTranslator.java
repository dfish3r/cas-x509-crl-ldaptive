package org.jasig.cas.server.authentication;

import org.springframework.ldap.AuthenticationException;

import javax.naming.NamingException;
import java.security.GeneralSecurityException;

/**
 * Implementation that doesn't do anything.  Useful only when we don't have an actual mapping.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class NullGeneralSecurityExceptionTranslator implements GeneralSecurityExceptionTranslator {

    public GeneralSecurityException translateExceptionIfPossible(final AuthenticationException e) {
        return null;
    }

    public GeneralSecurityException translateExceptionIfPossible(final NamingException e) {
        return null;
    }
}
