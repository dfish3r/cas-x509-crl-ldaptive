package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Authentication;

/**
 * NeverExpiresExpirationPolicy always answers false when asked if a Ticket is
 * expired. Use this policy when you want a Ticket to live forever, or at least
 * as long as the particular CAS Universe exists.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class NeverExpiresExpirationPolicy implements ExpirationPolicy {

    public boolean isExpired(final State state) {
        return false;
    }
}
