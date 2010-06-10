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

import com.github.inspektr.audit.annotation.Audit;
import org.perf4j.aop.Profiled;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

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

    @Profiled(tag="defaultAuthenticationManager_authenticate")
    @Audit(action="AUTHENTICATION", actionResolverName="AUTHENTICATION_RESOLVER", resourceResolverName="AUTHENTICATION_RESOURCE_RESOLVER")
    public AuthenticationResponse authenticate(final AuthenticationRequest authenticationRequest) {
        Assert.notEmpty(authenticationRequest.getCredentials(), "At least one credential is required.");

        final List<Credential> successfulCredentials = new ArrayList<Credential>();
        final List<Credential> failedCredentials = new ArrayList<Credential>();
        final List<GeneralSecurityException> authenticationExceptions = new ArrayList<GeneralSecurityException>();
        final Set<Authentication> successfulAuthentications = new HashSet<Authentication>();
        final List<Message> messages = new ArrayList<Message>();

        for (final Credential credential : authenticationRequest.getCredentials()) {
            try {
                final Authentication authentication = authenticateCredential(authenticationRequest, credential, messages);

                if (authentication == null) {
                    failedCredentials.add(credential);
                } else {
                    successfulCredentials.add(credential);
                    successfulAuthentications.add(authentication);

                }
            } catch (final GeneralSecurityException e) {
                authenticationExceptions.add(e);
                failedCredentials.add(credential);
            }
        }

        if (!failedCredentials.isEmpty() && this.isAllCredentialsMustSucceed()) {
            return new DefaultAuthenticationResponseImpl(authenticationExceptions, messages);
        }

        final AttributePrincipal principal = resolvePrincipal(successfulCredentials);

        if (principal == null) {
            return new DefaultAuthenticationResponseImpl(authenticationExceptions, messages);
        }

        return new DefaultAuthenticationResponseImpl(successfulAuthentications, principal, authenticationExceptions, messages);
    }

    protected Authentication authenticateCredential(final AuthenticationRequest request, final Credential c, final List<Message> messages) throws GeneralSecurityException {
        for (final AuthenticationHandler authenticationHandler : this.authenticationHandlers) {
            if (authenticationHandler.supports(c)) {
                final boolean value = authenticationHandler.authenticate(c);

                if (value) {
                    final Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();
                    for (final AuthenticationMetaDataResolver authenticationMetaDataResolver : getAuthenticationMetaDataResolvers()) {
                        attributes.putAll(authenticationMetaDataResolver.resolve(request, c));
                    }

                    for (final MessageResolver mr : getMessageResolvers()) {
                        messages.addAll(mr.resolveMessagesFor(c, authenticationHandler));
                    }

                    return getAuthenticationFactory().getAuthentication(attributes, request, authenticationHandler.getName());
                }
            }
        }
        return null;
    }

    protected AttributePrincipal resolvePrincipal(final List<Credential> successfulCredentials) {
        AttributePrincipal original = null;

        for (final Credential credential : successfulCredentials) {
            for (final CredentialToPrincipalResolver c  : this.credentialsToPrincipalResolvers) {
                if (c.supports(credential)) {
                    final AttributePrincipal p = c.resolve(credential);

                    if (p == null) {
                        log.debug(String.format("Credential [%s] did not resolve to a principal.  Aborting principal resolution.", credential));
                        return null;
                    }

                    if (original == null) {
                        original = p;
                        continue;
                    }

                    if (!original.getName().equals(p.getName())) {
                        log.debug(String.format("Credential [%s] with principal [%s] did not match prior principal [%s]", credential, p.getName(), original.getName()));
                        return null;
                    }
                }
            }
        }

        return original;
    }
}
