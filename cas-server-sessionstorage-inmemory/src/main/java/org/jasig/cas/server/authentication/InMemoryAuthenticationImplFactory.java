package org.jasig.cas.server.authentication;

import java.util.List;
import java.util.Map;

/**
 * Constructs a new {@link InMemoryAuthenticationImpl} from the provided
 * information.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class InMemoryAuthenticationImplFactory implements AuthenticationFactory {

    public Authentication getAuthentication(AttributePrincipal principal, Map<String, List<Object>> authenticationMetaData, final AuthenticationRequest authenticationRequest) {
        return new InMemoryAuthenticationImpl(principal, authenticationMetaData, authenticationRequest.isLongTermAuthenticationRequest());
    }
}
