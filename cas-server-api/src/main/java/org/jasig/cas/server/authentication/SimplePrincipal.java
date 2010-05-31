package org.jasig.cas.server.authentication;

import java.security.Principal;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class SimplePrincipal implements Principal {

    private String name;

    public SimplePrincipal(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
