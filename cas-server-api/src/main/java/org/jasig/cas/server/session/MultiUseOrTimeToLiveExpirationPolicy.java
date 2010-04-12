package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;

import javax.validation.constraints.Min;

/**
 * Allows for an item to either be used <code>maxNumberOfTimes</code> or <code>timeToLive</code> in seconds.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class MultiUseOrTimeToLiveExpirationPolicy implements ExpirationPolicy {

    @Min(1)
    private int maxNumberOfUses;

    @Min(1)
    private long timeToLive;

    /**
     * Creates a new MultiUseOrTimeToLiveExpirationPolicy with the maximum number of uses or the time to live (in seconds).
     *
     * @param maxNumberOfUses the maximum number of uses
     * @param timeToLive time to live, in seconds.
     */
    public MultiUseOrTimeToLiveExpirationPolicy(final int maxNumberOfUses, final long timeToLive) {
        this.maxNumberOfUses = maxNumberOfUses;
        this.timeToLive = timeToLive * 1000;
    }

    public MultiUseOrTimeToLiveExpirationPolicy(final long timeToLive) {
        this(Integer.MAX_VALUE, timeToLive);

    }

    public boolean isExpired(final State state) {
        if (state.getUsageCount() >= this.maxNumberOfUses) {
            return true;
        }

        if (System.currentTimeMillis() - state.getCreationTime() >= this.timeToLive) {
            return true;
        }

        return false;
    }
}
