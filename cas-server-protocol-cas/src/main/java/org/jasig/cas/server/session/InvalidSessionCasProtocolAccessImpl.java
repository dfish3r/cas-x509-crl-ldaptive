package org.jasig.cas.server.session;

import org.jasig.cas.server.login.ServiceAccessRequest;

/**
 * Created by IntelliJ IDEA.
 * User: battags
 * Date: 1/16/11
 * Time: 1:29 AM
 * To change this template use File | Settings | File Templates.
 */
public final class InvalidSessionCasProtocolAccessImpl extends AbstractInvalidAccess {

    private final ServiceAccessRequest serviceAccessRequest;

    public InvalidSessionCasProtocolAccessImpl(final ServiceAccessRequest serviceAccessRequest) {
        this.serviceAccessRequest = serviceAccessRequest;
    }

    public AccessResponseResult generateResponse(final AccessResponseRequest accessResponseRequest) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
