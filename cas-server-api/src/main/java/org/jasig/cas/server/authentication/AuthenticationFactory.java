package org.jasig.cas.server.authentication;

import java.util.List;
import java.util.Map;

/**
 * Factory for creating Authentication objects.  We have various factories because depending on which backing mechanism
 * you are using, you may construct different Authentication objects.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface AuthenticationFactory {

    /**
     * Constructs a new Authentication object based on the provided principal and authentication meta data.
     *
     * @param principal the person we have authenticated.
     * @param authenticationMetaData any information about the authentication.
     * @param authenticationRequest the original request for authentication.
     * @return a newly constructed authentication object.  This should NEVER return null, as only one should ever
     * be configured.
     */
    Authentication getAuthentication(AttributePrincipal principal, Map<String, List<Object>> authenticationMetaData, AuthenticationRequest authenticationRequest);
}
