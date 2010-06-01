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

import com.github.inspektr.audit.annotation.Audit;
import org.jasig.cas.server.authentication.*;
import org.perf4j.aop.Profiled;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.security.GeneralSecurityException;
import java.util.Map;

/**
 * Ensures that all authentication handlers are tried, but if one is tried, the associated CredentialsToPrincipalResolver is used.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.3.5
 */
public final class LinkedAuthenticationHandlerAndCredentialsToPrincipalResolverAuthenticationManager implements AuthenticationManager {

    @NotNull
    @Size(min = 1)
    private final Map<AuthenticationHandler, CredentialToPrincipalResolver> linkedHandlers;

    public LinkedAuthenticationHandlerAndCredentialsToPrincipalResolverAuthenticationManager(final Map<AuthenticationHandler,CredentialToPrincipalResolver> linkedHandlers) {
        this.linkedHandlers = linkedHandlers; 
    }

    // TODO implement

    @Profiled(tag="defaultAuthenticationManager_authenticate")
    @Audit(action="AUTHENTICATION", actionResolverName="AUTHENTICATION_RESOLVER", resourceResolverName="AUTHENTICATION_RESOURCE_RESOLVER")
    public AuthenticationResponse authenticate(final AuthenticationRequest authenticationRequest) {
        for (final Credential credential : authenticationRequest.getCredentials()) {
            for (final Map.Entry<AuthenticationHandler, CredentialToPrincipalResolver> entry : this.linkedHandlers.entrySet()) {
                final AuthenticationHandler authenticationHandler = entry.getKey();
                final CredentialToPrincipalResolver credentialToPrincipalResolver = entry.getValue();

                if (!authenticationHandler.supports(credential)) {
                    continue;
                }
                try {
                    authenticationHandler.authenticate(credential);
                } catch (final GeneralSecurityException e) {

                }
            }
        }



        return null;
    }
}
