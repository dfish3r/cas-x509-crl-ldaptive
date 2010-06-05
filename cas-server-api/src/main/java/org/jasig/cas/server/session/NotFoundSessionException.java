/**
 * Concrete implementation of a CentralAuthenticationService, and also the
 * central, organizing component of CAS's internal implementation.
 * <p>
 * This class is thread safe.
 * <p>
 * This class has the following properties that must be set:
 * <ul>
 * <li> <code>ticketRegistry</code> - The Ticket Registry to maintain the list
 * of available tickets.</li>
 * <li> <code>serviceTicketRegistry</code> - Provides an alternative to configure separate registries for TGTs and ST in order to store them
 * in different locations (i.e. long term memory or short-term)</li>
 * <li> <code>authenticationManager</code> - The service that will handle
 * authentication.</li>
 * <li> <code>ticketGrantingTicketUniqueTicketIdGenerator</code> - Plug in to
 * generate unique secure ids for TicketGrantingTickets.</li>
 * <li> <code>serviceTicketUniqueTicketIdGenerator</code> - Plug in to
 * generate unique secure ids for ServiceTickets.</li>
 * <li> <code>ticketGrantingTicketExpirationPolicy</code> - The expiration
 * policy for TicketGrantingTickets.</li>
 * <li> <code>serviceTicketExpirationPolicy</code> - The expiration policy for
 * ServiceTickets.</li>
 * </ul>
 *
 * @author William G. Thompson, Jr.
 * @author Scott Battaglia
 * @author Dmitry Kopylenko
 * @version $Revision$ $Date$
 * @since 3.0
 */
package org.jasig.cas.server.session;

/**
/**
 * Thrown when a session cannot be found.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class NotFoundSessionException extends SessionException {

    public NotFoundSessionException(final String message) {
        super(message);
    }
}
