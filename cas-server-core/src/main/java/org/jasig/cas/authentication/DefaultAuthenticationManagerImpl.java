/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.authentication;

import java.security.GeneralSecurityException;
import java.util.*;

import com.github.inspektr.audit.annotation.Audit;
import org.jasig.cas.server.authentication.*;
import org.perf4j.aop.Profiled;

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

public final class DefaultAuthenticationManagerImpl implements AuthenticationManager {

    /** An array of authentication handlers. */
    @NotNull
    @Size(min=1)
    private final List<AuthenticationHandler> authenticationHandlers;

    /** An array of CredentialsToPrincipalResolvers. */
    @NotNull
    @Size(min=1)
    private final List<CredentialToPrincipalResolver> credentialsToPrincipalResolvers;

    @NotNull
    private final AuthenticationFactory authenticationFactory;

    @NotNull
    private List<AuthenticationMetaDataResolver> authenticationMetaDataResolvers = new ArrayList<AuthenticationMetaDataResolver>();

    private boolean allCredentialsMustSucceed = true;

    public DefaultAuthenticationManagerImpl(final List<AuthenticationHandler> authenticationHandlers, final List<CredentialToPrincipalResolver> credentialsToPrincipalResolvers, final AuthenticationFactory authenticationFactory) {
        this.authenticationHandlers = authenticationHandlers;
        this.credentialsToPrincipalResolvers = credentialsToPrincipalResolvers;
        this.authenticationFactory = authenticationFactory;
    }

    @Profiled(tag="defaultAuthenticationManager_authenticate")
    @Audit(action="AUTHENTICATION", actionResolverName="AUTHENTICATION_RESOLVER", resourceResolverName="AUTHENTICATION_RESOURCE_RESOLVER")
    // TODO this algorithm is WRONG
    public AuthenticationResponse authenticate(final AuthenticationRequest authenticationRequest) {
        final List<Credential> successfulCredentials = new ArrayList<Credential>();
        final List<Credential> failedCredentials = new ArrayList<Credential>();
        final List<GeneralSecurityException> authenticationExceptions = new ArrayList<GeneralSecurityException>();

        if (authenticationRequest.getCredentials().isEmpty()) {
            throw new IllegalArgumentException("At least one form of credentials is required.");
        }

        // authenticate all credentials
        for (final Credential credential : authenticationRequest.getCredentials()) {
            try {
                final boolean value = authenticateCredential(credential);

                if (value) {
                    successfulCredentials.add(credential);
                } else {
                    failedCredentials.add(credential);
                }

            } catch (final GeneralSecurityException e) {
                authenticationExceptions.add(e);
                failedCredentials.add(credential);
            }
        }

        final List<Message> messages = new ArrayList<Message>();

        if (!failedCredentials.isEmpty() && this.allCredentialsMustSucceed) {
            return new DefaultAuthenticationResponseImpl(authenticationExceptions, messages);
        }

        final AttributePrincipal principal = resolvePrincipal(successfulCredentials);

        if (principal == null) {
            return new DefaultAuthenticationResponseImpl(authenticationExceptions, messages);
        }

        final Map<String, List<Object>> authenticationAttributes = new HashMap<String, List<Object>>();

        for (final AuthenticationMetaDataResolver authenticationMetaDataResolver : this.authenticationMetaDataResolvers) {
            authenticationAttributes.putAll(authenticationMetaDataResolver.resolve(authenticationRequest, successfulCredentials, principal));
        }

        final Authentication authentication = this.authenticationFactory.getAuthentication(principal, authenticationAttributes, authenticationRequest);


        return new DefaultAuthenticationResponseImpl(new HashSet<Authentication>(Arrays.asList(authentication)), authenticationExceptions, messages);
    }

    protected boolean authenticateCredential(final Credential c) throws GeneralSecurityException {
        for (final AuthenticationHandler authenticationHandler : this.authenticationHandlers) {
            if (authenticationHandler.supports(c)) {
                final boolean value = authenticationHandler.authenticate(c);

                if (value) {
                    return true;
                }
            }
        }
        return false;
    }

    // TODO implement a better algorithm since we need them all to actually match
    protected AttributePrincipal resolvePrincipal(final List<Credential> successfulCredentials) {

        for (final Credential credential : successfulCredentials) {
            for (final CredentialToPrincipalResolver c  : this.credentialsToPrincipalResolvers) {
                if (c.supports(credential)) {
                    final AttributePrincipal p = c.resolve(credential);

                    if (p != null) {
                        return p;
                    }
                }
            }
        }

        return null;
    }

    public void setAuthenticationMetaDataResolvers(final List<AuthenticationMetaDataResolver> authenticationMetaDataResolvers) {
        this.authenticationMetaDataResolvers = authenticationMetaDataResolvers;
    }

    public void setAllCredentialsMustSucceed(final boolean allCredentialsMustSucceed) {
        this.allCredentialsMustSucceed = allCredentialsMustSucceed;
    }
}
