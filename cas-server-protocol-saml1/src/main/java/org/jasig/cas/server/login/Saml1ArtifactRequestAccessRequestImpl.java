package org.jasig.cas.server.login;

import org.jasig.cas.server.session.Access;

/**
 * Represents a SAML1 request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class Saml1ArtifactRequestAccessRequestImpl extends DefaultLoginRequestImpl implements ServiceAccessRequest {

    private final String serviceId;

    public Saml1ArtifactRequestAccessRequestImpl(final String sessionId, final String remoteIpAddress, final boolean forceAuthentication, final boolean passiveAuthentication, final String serviceId) {
        super(sessionId, remoteIpAddress, forceAuthentication, passiveAuthentication, null);
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public String getPassiveAuthenticationRedirectUrl() {
        return this.serviceId;
    }
}
