package org.jasig.cas.server.session;

import org.jasig.cas.server.login.ServiceAccessRequest;
import org.jasig.cas.server.login.TokenServiceAccessRequest;

/**
 * Generates the invalid result responses.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class InvalidCasRequestProtocolAccessImpl extends AbstractInvalidAccess {

    private final ServiceAccessRequest serviceAccessRequest;

    public InvalidCasRequestProtocolAccessImpl(final ServiceAccessRequest serviceAccessRequest) {
        this.serviceAccessRequest = serviceAccessRequest;
    }

    public AccessResponseResult generateResponse(final AccessResponseRequest accessResponseRequest) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
