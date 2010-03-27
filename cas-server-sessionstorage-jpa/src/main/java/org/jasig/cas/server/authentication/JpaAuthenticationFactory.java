package org.jasig.cas.server.authentication;

import java.util.List;
import java.util.Map;

/**
 * Constructs a new {@link org.jasig.cas.server.authentication.JpaAuthenticationImpl}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class JpaAuthenticationFactory implements AuthenticationFactory {

    public Authentication getAuthentication(final AttributePrincipal principal, final Map<String, List<Object>> authenticationMetaData, final AuthenticationRequest authenticationRequest) {
        return new JpaAuthenticationImpl(principal, authenticationRequest.isLongTermAuthenticationRequest(), authenticationMetaData);
    }
}
