package org.jasig.cas.server.authentication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Immutable request for authentication.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class AuthenticationRequestImpl implements AuthenticationRequest {

    private final Date authenticationRequestDate = new Date();

    private final List<Credential> credentials = new ArrayList<Credential>();

    private final boolean longTermAuthenticationRequest;

    public AuthenticationRequestImpl(final List<Credential> credentials, final boolean longTermAuthenticationRequest) {
        this.credentials.addAll(credentials);
        this.longTermAuthenticationRequest = longTermAuthenticationRequest;
    }

    public Date getAuthenticationRequestDate() {
        return new Date(this.authenticationRequestDate.getTime());
    }

    public List<Credential> getCredentials() {
        return Collections.unmodifiableList(credentials);
    }

    public boolean isLongTermAuthenticationRequest() {
        return this.longTermAuthenticationRequest;
    }
}