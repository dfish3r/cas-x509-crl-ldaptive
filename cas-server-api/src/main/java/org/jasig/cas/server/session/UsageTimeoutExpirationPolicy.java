package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;

import javax.validation.constraints.Min;

/**
 * Expiration policy that is based on a certain time period for a ticket to
 * exist.
 * <p>
 * The expiration policy defined by this class is one of inactivity.  If you are inactive for the specified
 * amount of time, the ticket will be expired.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class UsageTimeoutExpirationPolicy implements ExpirationPolicy {

    /** The time to kill in milliseconds. */
    @Min(1)
    private final long timeToKillInMilliSeconds;

    public UsageTimeoutExpirationPolicy(final long timeToKillInMilliSeconds) {
        this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
    }

    public boolean isExpired(final State state) {
        return System.currentTimeMillis() - state.getLastUsedTime() >= this.timeToKillInMilliSeconds;
    }
}
