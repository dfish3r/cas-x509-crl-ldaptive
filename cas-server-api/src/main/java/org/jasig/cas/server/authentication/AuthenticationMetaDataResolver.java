package org.jasig.cas.server.authentication;

import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * The AuthenticationMetaDataResolver allows one to determine authentication attributes based on the original request,
 * the principal created from the request, and the validated credentials.
 * <p>
 * Meta data can be things like the type of authentication method used, etc.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface AuthenticationMetaDataResolver {

    /**
     * Derives specific authentication attributes based on the original request, the Credentials that were successfully
     * authenticated and the Principal it was resolved to.
     *
     * @param request the original request for authentication.
     * @param credentials the credentials that were successfully validated.
     * @param principal the resolved principal.
     * @return any attributes that correlate to the provided values.  This can be empty, but CANNOT be null.
     */
    Map<String, List<Object>> resolve(AuthenticationRequest request, List<Credential> credentials, Principal principal);
}
