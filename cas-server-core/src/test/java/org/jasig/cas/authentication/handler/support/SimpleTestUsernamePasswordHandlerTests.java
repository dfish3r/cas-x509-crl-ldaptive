/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.authentication.handler.support;

import org.jasig.cas.TestUtils;

import junit.framework.TestCase;
import org.jasig.cas.server.authentication.PlainTextPasswordEncoder;
import org.jasig.cas.server.authentication.UserNamePasswordCredential;

import java.security.GeneralSecurityException;

/**
 * Test of the simple username/password handler
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class SimpleTestUsernamePasswordHandlerTests extends TestCase {

    private SimpleTestUsernamePasswordAuthenticationHandler authenticationHandler;

    protected void setUp() throws Exception {
        this.authenticationHandler = new SimpleTestUsernamePasswordAuthenticationHandler();
        this.authenticationHandler
            .setPasswordEncoder(new PlainTextPasswordEncoder());
    }

    public void testSupportsProperUserCredentials() {
        assertTrue(this.authenticationHandler.supports(TestUtils
            .getCredentialsWithSameUsernameAndPassword()));
    }

    public void testDoesntSupportBadUserCredentials() {
        assertFalse(this.authenticationHandler.supports(TestUtils
            .getHttpBasedServiceCredentials()));
    }

    public void testValidUsernamePassword() throws GeneralSecurityException {
        assertTrue(this.authenticationHandler.authenticate(TestUtils
            .getCredentialsWithSameUsernameAndPassword()));
    }

    public void testInvalidUsernamePassword() {
        try {
            assertFalse(this.authenticationHandler.authenticate(TestUtils
                .getCredentialsWithDifferentUsernameAndPassword()));
        } catch (GeneralSecurityException ae) {
            // this is okay
        }
    }

    public void testNullUsernamePassword() {
        try {
            assertFalse(this.authenticationHandler.authenticate(TestUtils
                .getCredentialsWithSameUsernameAndPassword(null)));
        } catch (GeneralSecurityException ae) {
            // this is okay
        }
    }
    
    public void testAlternateClass() {
        this.authenticationHandler.setClassToSupport(UserNamePasswordCredential.class);
        assertTrue(this.authenticationHandler.supports(new UserNamePasswordCredential() {
            public String getUserName() {
                return null;
            }

            public String getPassword() {
                return null;
            }
        }));
    }
    
    public void testAlternateClassWithSubclassSupport() {
        this.authenticationHandler.setClassToSupport(UserNamePasswordCredential.class);
        this.authenticationHandler.setSupportSubClasses(true);
        assertTrue(this.authenticationHandler.supports(new ExtendedCredentials()));
    }
    
    public void testAlternateClassWithNoSubclassSupport() {
        this.authenticationHandler.setClassToSupport(UserNamePasswordCredential.class);
        this.authenticationHandler.setSupportSubClasses(false);
        assertFalse(this.authenticationHandler.supports(new ExtendedCredentials()));
    }
    
    protected class ExtendedCredentials implements UserNamePasswordCredential {

        private static final long serialVersionUID = 406992293105518363L;
        // nothing to see here


        public String getUserName() {
            return null;
        }

        public String getPassword() {
            return null;
        }
    }
}