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