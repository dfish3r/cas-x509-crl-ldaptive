package org.jasig.cas.server.session;

import org.jasig.cas.server.login.ServiceAccessRequest;

/**
 * Constructs a specific Access object based on the {@link org.jasig.cas.server.login.ServiceAccessRequest}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface AccessFactory {

    /**
     * Creates an Access object based on the {@link org.jasig.cas.server.login.ServiceAccessRequest}. CAN return null
     * if it does not know how to process a specific ServiceAccessRequest.
     *
     * @param session the existing session.
     * @param serviceAccessRequest the service access request.
     * @return an Access object, or NULL.
     */
    Access getAccess(Session session, ServiceAccessRequest serviceAccessRequest);
}
