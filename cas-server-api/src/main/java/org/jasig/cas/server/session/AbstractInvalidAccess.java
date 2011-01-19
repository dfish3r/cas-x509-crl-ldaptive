package org.jasig.cas.server.session;

import org.jasig.cas.server.login.TokenServiceAccessRequest;

/**
 * Abstract implementation to handle the methods that don't matter for invalid requests.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public abstract class AbstractInvalidAccess implements Access {

    public final String getId() {
        return null;
    }

    public final Session getSession() {
        return null;
    }

    public final String getResourceIdentifier() {
        return null;
    }

    public final void validate(final TokenServiceAccessRequest tokenServiceAccessRequest) {
        throw new UnsupportedOperationException();
    }

    public final boolean invalidate() {
        return false;
    }

    public final boolean isLocalSessionDestroyed() {
        return false;
    }

    public final boolean requiresStorage() {
        return false;
    }

    public final boolean isUsed() {
        return false;
    }
}
