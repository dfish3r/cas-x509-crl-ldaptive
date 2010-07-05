package org.jasig.cas.server.session;

/**
 * Default (and probably the only needed version) of the {@link org.jasig.cas.server.session.SessionStorageStatistics}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultSessionStorageStatisticsImpl implements SessionStorageStatistics {

    private final boolean validStatistics;

    private int countOfActiveSessions;

    private int countOfInactiveSessions;

    private int countOfUnusedAccesses;

    private int countOfUsedAccesses;

    public DefaultSessionStorageStatisticsImpl(final boolean validStatistics) {
        this.validStatistics = validStatistics;

        if (!validStatistics) {
            this.countOfInactiveSessions = -1;
            this.countOfUnusedAccesses = -1;
            this.countOfUsedAccesses = -1;
            this.countOfActiveSessions = -1;
        }
    }

    public boolean isValidStatistics() {
        return this.validStatistics;
    }

    public int getCountOfActiveSessions() {
        return this.countOfActiveSessions;
    }

    public int getCountOfInactiveSessions() {
        return this.countOfInactiveSessions;
    }

    public int getCountOfUnusedAccesses() {
        return this.countOfUnusedAccesses;
    }

    public int getCountOfUsedAccesses() {
        return this.countOfUsedAccesses;
    }

    public void incrementCountOfActiveSessions() {
        this.countOfActiveSessions++;
    }

    public void incrementCountOfInactiveSessions() {
        this.countOfInactiveSessions++;
    }

    public void incrementCountOfUnusedAccesses() {
        this.countOfUnusedAccesses++;
    }

    public void incrementCountOfUsedAccesses() {
        this.countOfUsedAccesses++;
    }
}
