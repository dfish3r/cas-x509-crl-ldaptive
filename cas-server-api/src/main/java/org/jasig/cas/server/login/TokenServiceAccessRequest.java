package org.jasig.cas.server.login;

/**
 * Represents a request to access a service when a token of a previous service access request has been provided.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface TokenServiceAccessRequest extends ServiceAccessRequest {

    /**
     * The token identifier for a previously generated {@link ServiceAccessRequest}. This CANNOT be null.
     *
     * @return the token representing the original request.
     */
    String getToken();
}
