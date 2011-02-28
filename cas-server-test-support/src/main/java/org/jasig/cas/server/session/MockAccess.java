package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.login.TokenServiceAccessRequest;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class MockAccess implements Access, SerializableSessionAware {

    private final String id;

    private final String resourceIdentifier;

    private Session session;

    public MockAccess(final String id, final String resourceIdentifier) {
        this.id = id;
        this.resourceIdentifier = resourceIdentifier;
    }

    public String getId() {
        return this.id;
    }

    public String getResourceIdentifier() {
        return this.resourceIdentifier;
    }

    public void validate(TokenServiceAccessRequest tokenServiceAccessRequest) {

    }

    public boolean invalidate() {
        return false;
    }

    public boolean isLocalSessionDestroyed() {
        return false;
    }

    public boolean requiresStorage() {
        return true;
    }

    public boolean isUsed() {
        return false;
    }

    public Session createDelegatedSession(AuthenticationResponse authenticationResponse) throws InvalidatedSessionException {
        return null;
    }

    public AccessResponseResult generateResponse(AccessResponseRequest accessResponseRequest) {
        return null;
    }

    public void setSession(final Session session) {
        this.session = session;
    }
}
