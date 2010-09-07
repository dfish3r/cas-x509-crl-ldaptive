package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;

/**
 * Abstract class purely to implement the natural ordering.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractAuthenticationImpl implements Authentication {

    public int compareTo(final Authentication o) {
        return this.getAuthenticationDate().compareTo(o.getAuthenticationDate());
    }
}
