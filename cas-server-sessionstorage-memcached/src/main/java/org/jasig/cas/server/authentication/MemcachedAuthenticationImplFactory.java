package org.jasig.cas.server.authentication;

import java.util.List;
import java.util.Map;

/**
 * Constructs a new {@link org.jasig.cas.server.authentication.MemcachedAuthenticationImpl} from the provided
 * information.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class MemcachedAuthenticationImplFactory implements AuthenticationFactory {

    public Authentication getAuthentication(AttributePrincipal principal, Map<String, List<Object>> authenticationMetaData, AuthenticationRequest authenticationRequest) {
        return new MemcachedAuthenticationImpl(principal, authenticationMetaData, authenticationRequest.isLongTermAuthenticationRequest());
    }
}
