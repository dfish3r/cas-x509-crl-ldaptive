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

    public AuthenticationResponse authenticate(final AuthenticationRequest authenticationRequest) {
        final List<Credential> failedCredentials = new ArrayList<Credential>();
        final List<GeneralSecurityException> authenticationExceptions = new ArrayList<GeneralSecurityException>();
        final Set<Authentication> successfulAuthentications = new HashSet<Authentication>();
        final List<Message> messages = new ArrayList<Message>();
        final List<AttributePrincipal> successPrincipals = new ArrayList<AttributePrincipal>();


        for (final Credential credential : authenticationRequest.getCredentials()) {
            final DirectAuthenticationHandlerMappingHolder holder = this.credentialsMapping.get(credential.getClass());

            Assert.notNull(holder, "missing mapping");

            final AuthenticationHandler handler = holder.getAuthenticationHandler();

            try {
                final boolean value = handler.authenticate(credential);

                if (value) {
                    final Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();

                    for (final AuthenticationMetaDataResolver resolve : getAuthenticationMetaDataResolvers()) {
                        attributes.putAll(resolve.resolve(authenticationRequest, credential));
                    }

                    for (final MessageResolver messageResolver : getMessageResolvers()) {
                        messages.addAll(messageResolver.resolveMessagesFor(credential, handler));
                    }

                    final AttributePrincipal principal = holder.getCredentialsToPrincipalResolver().resolve(credential);

                    if (principal == null) {
                        failedCredentials.add(credential);
                        continue;
                    }

                    final Authentication authentication = getAuthenticationFactory().getAuthentication(attributes, authenticationRequest, handler.getName());
                    successfulAuthentications.add(authentication);
                    successPrincipals.add(principal);
                } else {
                    failedCredentials.add(credential);
                }
            } catch (final GeneralSecurityException e) {
                failedCredentials.add(credential);
                authenticationExceptions.add(e);
            }
        }

        if (!failedCredentials.isEmpty() && this.isAllCredentialsMustSucceed()) {
            return new DefaultAuthenticationResponseImpl(authenticationExceptions, messages);
        }

        final AttributePrincipal principal = resolvePrincipal(successPrincipals);

        if (principal == null) {
            return new DefaultAuthenticationResponseImpl(authenticationExceptions, messages);
        }

        return new DefaultAuthenticationResponseImpl(successfulAuthentications, principal, authenticationExceptions, messages);
    }

    protected AttributePrincipal resolvePrincipal(final List<AttributePrincipal> successfulPrincipals) {
        AttributePrincipal principal = null;

        for (final AttributePrincipal p : successfulPrincipals) {
            if (principal == null) {
                principal = p;
                continue;
            }

            if (!principal.getName().equals(p.getName())) {
                return null;
            }
        }

        return principal;
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
