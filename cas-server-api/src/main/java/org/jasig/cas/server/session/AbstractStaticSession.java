package org.jasig.cas.server.session;

import java.util.List;

/**
 * Abstract class to help implementations of the {@link org.jasig.cas.server.session.SessionStorage} interface
 * that don't store object in memory (and thus don't want to serialize some stuff).
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractStaticSession extends AbstractSession {

    private static ExpirationPolicy EXPIRATION_POLICY;

    private static List<AccessFactory> ACCESS_FACTORIES;

    public static void setExpirationPolicy(final ExpirationPolicy expirationPolicy) {
        EXPIRATION_POLICY = expirationPolicy;
    }

    public static void setAccessFactories(final List<AccessFactory> accessFactories) {
        ACCESS_FACTORIES = accessFactories;
    }

    protected final List<AccessFactory> getAccessFactories() {
        return ACCESS_FACTORIES;
    }

    protected final ExpirationPolicy getExpirationPolicy() {
        return EXPIRATION_POLICY;
    }
}
