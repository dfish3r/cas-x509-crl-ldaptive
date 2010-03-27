package org.jasig.cas.server.session;

/**
 * Generic exception if there is an issue with the session.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class SessionException extends Exception {

    protected SessionException(final String message) {
        super(message);
    }
}
