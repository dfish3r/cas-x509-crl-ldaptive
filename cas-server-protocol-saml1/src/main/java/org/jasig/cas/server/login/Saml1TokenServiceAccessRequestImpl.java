package org.jasig.cas.server.login;

/**
 * Constructs a request used to validate a SAML1 request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class Saml1TokenServiceAccessRequestImpl extends DefaultTokenServiceAccessRequestImpl {

    public Saml1TokenServiceAccessRequestImpl(final String sessionId, final String remoteIpAddress, final boolean forceAuthentication, final String token, final String serviceId) {
        super(sessionId, remoteIpAddress, forceAuthentication, token, serviceId);
    }
}
