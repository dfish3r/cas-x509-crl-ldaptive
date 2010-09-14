package org.jasig.cas.server.login;

import org.apache.commons.lang.StringUtils;

import java.util.Map;

/**
 * Constructs a new SAML1 request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class Saml1ArtifactRequestAccessRequestImplFactory extends AbstractServiceAccessRequestFactory {

    private static final String CONST_SERVICE_PARAM = "TARGET";

    public ServiceAccessRequest getServiceAccessRequest(final String sessionId, final String remoteIpAddress, final Map parameters) {
        final String service = getValue(parameters.get(CONST_SERVICE_PARAM));

        if (StringUtils.isBlank(service)) {
            return null;
        }

        return new Saml1ArtifactRequestAccessRequestImpl(sessionId, remoteIpAddress, false, false, service);
    }
}
