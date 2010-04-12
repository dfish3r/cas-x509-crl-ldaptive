/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.authentication;

import com.github.inspektr.audit.annotation.Audit;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.UnsupportedCredentialsException;
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
