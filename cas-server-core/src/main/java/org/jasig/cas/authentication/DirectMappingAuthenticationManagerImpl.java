/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
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
