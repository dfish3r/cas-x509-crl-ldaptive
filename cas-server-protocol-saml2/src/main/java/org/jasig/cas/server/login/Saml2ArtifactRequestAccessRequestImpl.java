package org.jasig.cas.server.login;

import org.jasig.cas.server.session.Access;

/**
 * Represents a SAML2 request.  At the moment, this has been tested with:
 * <ul>
 * <li>Google Apps</li>
 * <li>Salesforce.com</li>
 * </ul>
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class Saml2ArtifactRequestAccessRequestImpl extends DefaultLoginRequestImpl implements ServiceAccessRequest {

    private final String serviceId;

    public Saml2ArtifactRequestAccessRequestImpl(String sessionId, String remoteIpAddress, boolean forceAuthentication, boolean passiveAuthentication, Access access, final String serviceId) {
        super(sessionId, remoteIpAddress, forceAuthentication, passiveAuthentication, access);
        this.serviceId = serviceId;
    }

    public String getServiceId() {
        return this.serviceId;
    }

    public String getPassiveAuthenticationRedirectUrl() {
        return this.serviceId;
    }
}
