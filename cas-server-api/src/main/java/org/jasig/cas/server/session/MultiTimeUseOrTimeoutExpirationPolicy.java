package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;

import javax.validation.constraints.Min;

/**
 * ExpirationPolicy that is based on certain number of uses of a ticket or a
 * certain time period for a ticket to exist.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class MultiTimeUseOrTimeoutExpirationPolicy implements ExpirationPolicy {

    /** The time to kill in millseconds. */
    @Min(1)
    private final long timeToKillInMilliSeconds;

    /** The maximum number of uses before expiration. */
    @Min(1)
    private final int numberOfUses;

    public MultiTimeUseOrTimeoutExpirationPolicy(final int numberOfUses, final long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
        this.numberOfUses = numberOfUses;
    }

    public boolean isExpired(final State state) {
        return (state.getUsageCount() >= this.numberOfUses)
            || (System.currentTimeMillis() - state.getLastUsedTime() >= this.timeToKillInMilliSeconds);
    }
}
