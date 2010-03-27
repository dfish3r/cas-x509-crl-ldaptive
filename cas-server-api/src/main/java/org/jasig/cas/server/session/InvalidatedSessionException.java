package org.jasig.cas.server.session;

/**
 * Thrown when an invalidated session is used.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class InvalidatedSessionException extends SessionException {

    public InvalidatedSessionException(final String message) {
        super(message);
    }
}
