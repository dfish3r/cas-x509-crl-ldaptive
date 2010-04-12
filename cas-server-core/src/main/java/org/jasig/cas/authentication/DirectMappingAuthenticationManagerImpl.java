/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.authentication;

import java.util.Map;

import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.server.authentication.*;
import org.springframework.util.Assert;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * Authentication Manager that provides a direct mapping between credentials
 * provided and the authentication handler used to authenticate the user.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public final class DirectMappingAuthenticationManagerImpl implements AuthenticationManager {

    @NotNull
    @Size(min=1)
    private Map<Class< ? extends Credential>, DirectAuthenticationHandlerMappingHolder> credentialsMapping;

    // TODO implement
    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

/*
    @Override
    protected Pair<AuthenticationHandler, AttributePrincipal> authenticateAndObtainPrincipal(final Credential credentials) throws AuthenticationException {
        final Class< ? extends Credential> credentialsClass = credentials.getClass();
        final DirectAuthenticationHandlerMappingHolder d = this.credentialsMapping
            .get(credentialsClass);

        Assert.notNull(d, "no mapping found for: " + credentialsClass.getName());

        if (!d.getAuthenticationHandler().authenticate(credentials)) {
            throw new BadCredentialsAuthenticationException();
        }

        final AttributePrincipal p = d.getCredentialsToPrincipalResolver().resolve(credentials);

        return new Pair<AuthenticationHandler, AttributePrincipal>(d.getAuthenticationHandler(), p);
    }
    */

    public final void setCredentialsMapping(
        final Map<Class< ? extends Credential>, DirectAuthenticationHandlerMappingHolder> credentialsMapping) {
        this.credentialsMapping = credentialsMapping;
    }

    public static final class DirectAuthenticationHandlerMappingHolder {

        private AuthenticationHandler authenticationHandler;

        private CredentialToPrincipalResolver credentialsToPrincipalResolver;

        public DirectAuthenticationHandlerMappingHolder() {
            // nothing to do
        }

        public final AuthenticationHandler getAuthenticationHandler() {
            return this.authenticationHandler;
        }

        public void setAuthenticationHandler(
            final AuthenticationHandler authenticationHandler) {
            this.authenticationHandler = authenticationHandler;
        }

        public CredentialToPrincipalResolver getCredentialsToPrincipalResolver() {
            return this.credentialsToPrincipalResolver;
        }

        public void setCredentialToPrincipalResolver(final CredentialToPrincipalResolver credentialsToPrincipalResolver) {
            this.credentialsToPrincipalResolver = credentialsToPrincipalResolver;
        }
    }

}
