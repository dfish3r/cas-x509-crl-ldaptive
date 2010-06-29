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

package org.jasig.cas.server.authentication;

import org.springframework.util.Assert;

import java.security.GeneralSecurityException;
import java.util.*;

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
public final class DirectMappingAuthenticationManagerImpl extends AbstractAuthenticationManager {

    @NotNull
    @Size(min=1)
    private final Map<Class< ? extends Credential>, DirectAuthenticationHandlerMappingHolder> credentialsMapping;

    public DirectMappingAuthenticationManagerImpl(final Map<Class< ? extends Credential>, DirectAuthenticationHandlerMappingHolder> credentialsMapping, final AuthenticationFactory authenticationFactory) {
        super(authenticationFactory);
        this.credentialsMapping = credentialsMapping;
    }

    @Override
    protected void obtainAuthenticationsAndPrincipals(final AuthenticationRequest authenticationRequest, final Collection<Authentication> authentications, final Collection<AttributePrincipal> principals, final Collection<GeneralSecurityException> exceptions, final Collection<Message> messages) {
        for (final Credential credential : authenticationRequest.getCredentials()) {
            final DirectAuthenticationHandlerMappingHolder holder = this.credentialsMapping.get(credential.getClass());

            if (holder == null) {
                log.warn(String.format("No handler mapping found for %s", credential.getClass().getSimpleName()));
                continue;
            }

            final AuthenticationHandler handler = holder.getAuthenticationHandler();

            try {

                if (handler.authenticate(credential)) {
                    final AttributePrincipal principal = holder.getCredentialsToPrincipalResolver().resolve(credential);

                    if (principal != null) {
                        final Map<String, List<Object>> attributes = obtainAttributesFor(authenticationRequest, credential);
                        obtainMessagesFor(credential, handler, messages);
                        authentications.add(getAuthenticationFactory().getAuthentication(attributes, authenticationRequest, handler.getName()));
                        principals.add(principal);
                    }
                }
            } catch (final GeneralSecurityException e) {
                exceptions.add(e);
            }
        }
    }

    public static final class DirectAuthenticationHandlerMappingHolder {

        private AuthenticationHandler authenticationHandler;

        private CredentialToPrincipalResolver credentialsToPrincipalResolver;

        public DirectAuthenticationHandlerMappingHolder() {
            // nothing to do
        }

        public DirectAuthenticationHandlerMappingHolder(final AuthenticationHandler authenticationHandler, final CredentialToPrincipalResolver credentialToPrincipalResolver) {
            this.authenticationHandler = authenticationHandler;
            this.credentialsToPrincipalResolver = credentialToPrincipalResolver;
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
