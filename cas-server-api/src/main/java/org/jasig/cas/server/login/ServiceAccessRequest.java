package org.jasig.cas.server.login;

/**
 * Request access to a specific service identified by the provided identifier.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public interface ServiceAccessRequest extends LoginRequest {

    /**
     * The identifier for the service.  This CANNOT be null.
     *
     * @return the identifier for the service.
     */
    String getServiceId();

    /**
     * The url to redirect to should passive authentication fail.
     *
     * @return the url to redirect to should passive authentication fail.  Can be null if protocol doesn't support.  However,
     * passiveAuthentication must also be set to false in that instance.
     */
    String getPassiveAuthenticationRedirectUrl();
}