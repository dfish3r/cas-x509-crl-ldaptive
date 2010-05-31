package org.jasig.cas.server.logout;

import org.jasig.cas.server.session.Access;
import org.jasig.cas.server.session.Session;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Default implementation of the {@link LogoutResponse}
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class DefaultLogoutResponseImpl implements LogoutResponse {

    private final Date date = new Date();

    private final List<Access> loggedOutAccesses;

    private final List<Access> loggedInAccesses;

    /**
     * Default constructor that reads the accesses from the session and sorts them into the appropriate lists.
     * <p>
     * Does NOT retain a link to the session.
     *
     * @param session the session to grab the accesses from.
     */
    public DefaultLogoutResponseImpl(final Session session) {
        final List<Access> loggedOutAccesses = new ArrayList<Access>();
        final List<Access> loggedInAccesses = new ArrayList<Access>();

        for (final Access access : session.getAccesses()) {
            if (access.isLocalSessionDestroyed()) {
                loggedOutAccesses.add(access);
            } else {
                loggedInAccesses.add(access);
            }
        }

        this.loggedInAccesses = Collections.unmodifiableList(loggedInAccesses);
        this.loggedOutAccesses = Collections.unmodifiableList(loggedOutAccesses);
    }

    public DefaultLogoutResponseImpl() {
        this.loggedInAccesses = new ArrayList<Access>();
        this.loggedOutAccesses = new ArrayList<Access>();
    }

    public Date getDate() {
        return new Date(this.date.getTime());
    }

    public List<Access> getLoggedOutAccesses() {
        return this.loggedOutAccesses;
    }

    public List<Access> getLoggedInAccesses() {
        return this.loggedInAccesses;
    }
}