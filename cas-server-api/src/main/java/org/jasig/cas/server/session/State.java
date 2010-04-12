package org.jasig.cas.server.session;

/**
 * Holds the internal state of an object so that an {@link org.jasig.cas.server.session.ExpirationPolicy} can be
 * used in conjunction with the object to determine its current status.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 *
 */
public interface State {

    /**
     * Update the internal state.  Only the object that OWNS this state should call this method.
     */
    void updateState();

    /**
     * Return the number of times this thing was "used."  Used is subjective and varies by thing holding state.
     * @return the usage count, >= 0
     */
    int getUsageCount();

    /**
     * The time the state was initialized.
     *
     * @return the creation time, in milliseconds past the epoch.
     */
    long getCreationTime();

    /**
     * The time the state was last "used."
     *
     * @return the time last used.  Cannot be less than the creation time.
     */
    long getLastUsedTime();

    /**
     * Its true, if one of the authentication's is a long term authentication request.
     *
     * @return true, if a long term authentication exists, false otherwise.
     */
    boolean longTermAuthenticationExists();
}
