package org.jasig.cas.server.session;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Abstract class to hold most of the items that are needed by implementations to construct a new
 * session.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSessionStorage implements SessionStorage {

    @NotNull
    @Size(min=1)
    private final List<AccessFactory> accessFactories;

    @NotNull
    private ExpirationPolicy expirationPolicy;

    protected AbstractSessionStorage(final List<AccessFactory> accessFactories) {
        this.accessFactories = accessFactories;
    }

    public final void setExpirationPolicy(final ExpirationPolicy expirationPolicy) {
        this.expirationPolicy = expirationPolicy;
    }

    protected final ExpirationPolicy getExpirationPolicy() {
        return this.expirationPolicy;
    }

    protected final List<AccessFactory> getAccessFactories() {
        return this.accessFactories;
    }
}
