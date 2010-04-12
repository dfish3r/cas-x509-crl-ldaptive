/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.server.session;

/**
 * Ticket expiration policy based on a hard timeout from ticket creation time rather than the
 * "idle" timeout provided by {@link org.jasig.cas.server.session.UsageTimeoutExpirationPolicy}.
 *
 * @author Andrew Feller
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1.2
 */
public final class HardTimeoutExpirationPolicy implements ExpirationPolicy {

	/** Unique Id for serialization. */
    private static final long serialVersionUID = -1465997330804816888L;
    
    /** The time to kill in milliseconds. */
	private final long timeToKillInMilliSeconds;

	public HardTimeoutExpirationPolicy(final long timeToKillInMilliSeconds) {
		this.timeToKillInMilliSeconds = timeToKillInMilliSeconds;
	}

    public boolean isExpired(final State state) {
        return (System.currentTimeMillis() - state.getCreationTime() >= this.timeToKillInMilliSeconds);
    }

}
