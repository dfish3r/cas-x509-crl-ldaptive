/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.authentication;

import java.util.Arrays;

import org.jasig.cas.AbstractCentralAuthenticationServiceTest;
import org.jasig.cas.TestUtils;
import org.jasig.cas.authentication.handler.AuthenticationException;
import org.jasig.cas.authentication.handler.BadCredentialsAuthenticationException;
import org.jasig.cas.authentication.handler.UnsupportedCredentialsException;
import org.jasig.cas.authentication.handler.support.HttpBasedServiceCredentialsAuthenticationHandler;
import org.jasig.cas.authentication.principal.UsernamePasswordCredentialsToPrincipalResolver;
import org.jasig.cas.server.authentication.AttributePrincipal;
import org.jasig.cas.server.authentication.AuthenticationHandler;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.authentication.CredentialToPrincipalResolver;
import org.jasig.cas.util.HttpClient;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class AuthenticationManagerImplTests extends AbstractCentralAuthenticationServiceTest {

    @Test
    public void testSuccessfulAuthentication() throws Exception {
        assertEquals(TestUtils.getPrincipal(),
            getAuthenticationManager().authenticate(
                TestUtils.getCredentialsWithSameUsernameAndPassword())
                .getPrincipal());
    }

    @Test
    public void testFailedAuthentication() throws Exception {
        try {
            getAuthenticationManager().authenticate(
                TestUtils.getCredentialsWithDifferentUsernameAndPassword());
            fail("Authentication should have failed.");
        } catch (AuthenticationException e) {
            // nothing to do
        }
    }

    @Test
    public void testNoHandlerFound() throws AuthenticationException {
        try {
            getAuthenticationManager().authenticate(new Credential() {

                private static final long serialVersionUID = -4897240037527663222L;
                // there is nothing to do here
            });
            fail("Authentication should have failed.");
        } catch (UnsupportedCredentialsException e) {
            return;
        }
    }

    @Test(expected=UnsupportedCredentialsException.class)
    public void testNoResolverFound() throws Exception {
        DefaultAuthenticationManagerImpl manager = new DefaultAuthenticationManagerImpl();
        HttpBasedServiceCredentialsAuthenticationHandler authenticationHandler = new HttpBasedServiceCredentialsAuthenticationHandler();
        authenticationHandler.setHttpClient(new HttpClient());
        manager.setAuthenticationHandlers(Arrays.asList((AuthenticationHandler) authenticationHandler));
        manager.setCredentialsToPrincipalResolvers(Arrays.asList((CredentialsToPrincipalResolver) new UsernamePasswordCredentialsToPrincipalResolver()));
            manager.authenticate(TestUtils.getHttpBasedServiceCredentials());
    }

    @Test(expected = BadCredentialsAuthenticationException.class)
    public void testResolverReturnsNull() throws Exception {
        DefaultAuthenticationManagerImpl manager = new DefaultAuthenticationManagerImpl();
        HttpBasedServiceCredentialsAuthenticationHandler authenticationHandler = new HttpBasedServiceCredentialsAuthenticationHandler();
        authenticationHandler.setHttpClient(new HttpClient());
        manager.setAuthenticationHandlers(Arrays.asList((AuthenticationHandler) authenticationHandler));
        manager
            .setCredentialsToPrincipalResolvers(Arrays.asList((CredentialsToPrincipalResolver) new TestCredentialsToPrincipalResolver()));
            manager.authenticate(TestUtils.getHttpBasedServiceCredentials());
    }
    
    protected class TestCredentialsToPrincipalResolver implements CredentialToPrincipalResolver {

        public AttributePrincipal resolve(Credential credentials) {
            return null;
        }

        public boolean supports(final Credential credentials) {
            return true;
        }
    }
}
