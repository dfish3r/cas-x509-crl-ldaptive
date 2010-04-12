package org.jasig.cas.server.session;

import java.io.Serializable;

/**
 * Default implementation of the {@link org.jasig.cas.server.session.State} interface suitable for in-memory storage or
 * serialization.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class SimpleStateImpl implements State, Serializable {

    private volatile int usageCount = 0;

    private final long creationTime = System.currentTimeMillis();

    private volatile long lastUsedTime = System.currentTimeMillis();

    private boolean longTermSession;

    public synchronized void updateState() {
        this.usageCount++;
        this.lastUsedTime = System.currentTimeMillis();
    }

    public int getUsageCount() {
        return this.usageCount;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public long getLastUsedTime() {
        return this.lastUsedTime;
    }

    public void setLongTermSessionExists(final boolean longTermSession) {
        this.longTermSession = longTermSession;
    }

    public boolean longTermAuthenticationExists() {
        return this.longTermSession;
    }
}
