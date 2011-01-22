package org.jasig.cas.server.login;

import org.jasig.cas.server.session.ServicesManager;
import org.jasig.cas.server.session.UnauthorizedServiceException;

import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
@Named("gatewayServicesManagementCheck")
public final class GatewayServicesManagementCheck {

    @NotNull
    private ServicesManager servicesManager;

    @Inject
    public GatewayServicesManagementCheck(final ServicesManager servicesManager) {
        this.servicesManager = servicesManager;
    }

    public void isServiceAllowedToGateway(final ServiceAccessRequest serviceAccessRequest) {
        final boolean match = this.servicesManager.matchesExistingService(serviceAccessRequest);

        if (match) {
            return;
        }

        throw new UnauthorizedServiceException(String.format("Service [%s] is not authorized to use CAS.", serviceAccessRequest.getServiceId()));
    }
}
