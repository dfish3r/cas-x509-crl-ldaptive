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

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Ensures that all authentication handlers are tried, but if one is tried, the associated CredentialsToPrincipalResolver is used.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.3.5
 */
public final class LinkedAuthenticationHandlerAndCredentialsToPrincipalResolverAuthenticationManagerImpl extends AbstractAuthenticationManager {

    @NotNull
    @Size(min = 1)
    private final Map<AuthenticationHandler, CredentialToPrincipalResolver> linkedHandlers;

    @Inject
    public LinkedAuthenticationHandlerAndCredentialsToPrincipalResolverAuthenticationManagerImpl(final Map<AuthenticationHandler,CredentialToPrincipalResolver> linkedHandlers, final AuthenticationFactory authenticationFactory) {
        super(authenticationFactory);
        this.linkedHandlers = linkedHandlers; 
    }

    @Override
    protected void obtainAuthenticationsAndPrincipals(final AuthenticationRequest authenticationRequest, final Collection<Authentication> authentications, final Collection<AttributePrincipal> principals, final Map<Credential, List<GeneralSecurityException>> exceptionMap, final Collection<Message> messages) {
        final List<Credential> credentials = authenticationRequest.getCredentials();

        for (final Credential credential : credentials) {
            final List<GeneralSecurityException> exceptions = new ArrayList<GeneralSecurityException>();
            for (final Map.Entry<AuthenticationHandler,CredentialToPrincipalResolver> entry : linkedHandlers.entrySet()) {
                final AuthenticationHandler handler = entry.getKey();
                final CredentialToPrincipalResolver resolver = entry.getValue();

                if (handler.supports(credential)) {
                    try {
                        if (handler.authenticate(credential) && resolver !=null) {
                            final AttributePrincipal p = resolver.resolve(credential);

                            if (p != null) {
                                principals.add(p);
                                final Map<String, List<Object>> attributes = obtainAttributesFor(authenticationRequest, credential);
                                authentications.add(getAuthenticationFactory().getAuthentication(attributes, authenticationRequest, handler.getName()));
                                obtainMessagesFor(credential, handler, messages);
                                break;
                            }
                        }
                    } catch (final GeneralSecurityException e) {
                        exceptions.add(e);
                    }
                }

                if (!exceptions.isEmpty()) {
                    exceptionMap.put(credential, exceptions);
                }
            }
        }
    }
}
