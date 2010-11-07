package org.jasig.cas.server.login;

/**
 * Represents a SAML1 request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
// TODO we need an abstract ServiceAccessRequestImpl that contains the passive authentication stuff
public final class Saml1ArtifactRequestAccessRequestImpl extends DefaultLoginRequestImpl implements ServiceAccessRequest {

    private final String serviceId;

    private final boolean passiveAuthentication;

    public Saml1ArtifactRequestAccessRequestImpl(final String sessionId, final String remoteIpAddress, final boolean forceAuthentication, final boolean passiveAuthentication, final String serviceId) {
        super(sessionId, remoteIpAddress, forceAuthentication, null);
        this.serviceId = serviceId;
        this.passiveAuthentication = passiveAuthentication;
    }

    public boolean isPassiveAuthentication() {
        return this.passiveAuthentication;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public String getPassiveAuthenticationRedirectUrl() {
        return this.passiveAuthentication ? this.serviceId : null;
    }
}
