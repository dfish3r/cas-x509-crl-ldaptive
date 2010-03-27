package org.jasig.cas.server.session;

import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 * JPA-annotated implementation of the {@link org.jasig.cas.server.session.State} interface.
 *
 * @author Scott Battaglia
 * @version $Revision$ Date$
 * @since 4.0.0
 */
@Embeddable
public final class JpaStateImpl implements State {

    @Column(name="session_state_usage_count", insertable = true, updatable = true, nullable = false)
    private volatile int count = 0;

    @Column(name="session_state_creation", insertable = true, updatable = false, nullable = false)
    private volatile long creationTime = System.currentTimeMillis();

    @Column(name="session_state_last_used",insertable = true, updatable = true, nullable = false)
    private long lastUsedTime = System.currentTimeMillis();

    public synchronized void updateState() {
        this.count++;
        this.lastUsedTime = System.currentTimeMillis();
    }

    public int getUsageCount() {
        return this.count;
    }

    public long getCreationTime() {
        return this.creationTime;
    }

    public long getLastUsedTime() {
        return this.lastUsedTime;
    }
}
