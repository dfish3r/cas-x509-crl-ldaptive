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

import java.security.GeneralSecurityException;
import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * <p>
 * Default implementation of the AuthenticationManager. The
 * AuthenticationManager follows the following algorithm. The manager loops
 * through the array of AuthenticationHandlers searching for one that can
 * attempt to determine the validity of the credentials. If it finds one, it
 * tries that one. If that handler returns true, it continues on. If it returns
 * false, it looks for another handler. If it throws an exception, it aborts the
 * whole process and rethrows the exception. Next, it looks for a
 * CredentialsToPrincipalResolver that can handle the credentials in order to
 * create a Principal. Finally, it attempts to populate the Authentication
 * object's attributes map using AuthenticationAttributesPopulators
 * <p>
 * Behavior is determined by external beans attached through three configuration
 * properties. The Credentials are opaque to the manager. They are passed to the
 * external beans to see if any can process the actual type represented by the
 * Credentials marker.
 * <p>
 * DefaultAuthenticationManagerImpl requires the following properties to be set:
 * </p>
 * <ul>
 * <li> <code>authenticationHandlers</code> - The array of
 * AuthenticationHandlers that know how to process the credentials provided.
 * <li> <code>credentialsToPrincipalResolvers</code> - The array of
 * CredentialsToPrincipal resolvers that know how to process the credentials
 * provided.
 * </ul>
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 * @see org.jasig.cas.server.authentication.AuthenticationHandler
 * @see org.jasig.cas.server.authentication.CredentialToPrincipalResolver
 * @see org.jasig.cas.server.authentication.AuthenticationMetaDataResolver
 */

public final class DefaultAuthenticationManagerImpl extends AbstractAuthenticationManager {

    private final Logger log = LoggerFactory.getLogger(getClass());

    /** An array of authentication handlers. */
    @NotNull
    @Size(min=1)
    private final List<AuthenticationHandler> authenticationHandlers;

    /** An array of CredentialsToPrincipalResolvers. */
    @NotNull
    @Size(min=1)
    private final List<CredentialToPrincipalResolver> credentialsToPrincipalResolvers;

    public DefaultAuthenticationManagerImpl(final List<AuthenticationHandler> authenticationHandlers, final List<CredentialToPrincipalResolver> credentialsToPrincipalResolvers, final AuthenticationFactory authenticationFactory) {
        super(authenticationFactory);
        this.authenticationHandlers = authenticationHandlers;
        this.credentialsToPrincipalResolvers = credentialsToPrincipalResolvers;
    }

    @Override
    protected void obtainAuthenticationsAndPrincipals(final AuthenticationRequest authenticationRequest, final Collection<Authentication> authentications, final Collection<AttributePrincipal> principals, final Map<Credential, List<GeneralSecurityException>> exceptionMap, final Collection<Message> messages) {

        for (final Credential credential : authenticationRequest.getCredentials()) {
            final List<GeneralSecurityException> exceptions = new ArrayList<GeneralSecurityException>();
            for (final AuthenticationHandler handler : this.authenticationHandlers) {
                if (!handler.supports(credential)) {
                    continue;
                }

                try {
                    handler.authenticate(credential);
                    final AttributePrincipal p = getAttributePrincipal(credential);

                    if (p == null) {
                        break;
                    }

                    final Map<String, List<Object>> attributes = obtainAttributesFor(authenticationRequest, credential);
                    obtainMessagesFor(credential, handler, messages);
                    authentications.add(getAuthenticationFactory().getAuthentication(attributes, authenticationRequest, handler.getName()));
                    principals.add(p);
                    break;
                } catch (final GeneralSecurityException e) {
                    exceptions.add(e);
                    break;
                }
            }

            if (!exceptions.isEmpty()) {
                exceptionMap.put(credential, exceptions);
            }
        }
    }

    protected AttributePrincipal getAttributePrincipal(final Credential c) {
        for (final CredentialToPrincipalResolver credentialToPrincipalResolver : this.credentialsToPrincipalResolvers) {
            if (credentialToPrincipalResolver.supports(c)) {
                final AttributePrincipal p = credentialToPrincipalResolver.resolve(c);

                if (p != null) {
                    return p;
                }
            }
        }

        return null;
    }
}
