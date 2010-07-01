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

package org.jasig.cas.adaptors.generic;

import java.net.MalformedURLException;
import java.net.URL;
import java.security.GeneralSecurityException;

import junit.framework.TestCase;

import org.jasig.cas.TestUtils;
import org.jasig.cas.server.authentication.DefaultUrlCredentialImpl;
import org.jasig.cas.server.authentication.UrlCredential;
import org.jasig.cas.server.authentication.UserNamePasswordCredential;
import org.springframework.core.io.ClassPathResource;

/**
 * @author Scott Battaglia
 * @version $Id$
 */
public class FileAuthenticationHandlerTests extends TestCase {

    private FileAuthenticationHandler authenticationHandler;

    /**
     * @see junit.framework.TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        this.authenticationHandler = new FileAuthenticationHandler();
        this.authenticationHandler.setFileName(new ClassPathResource("org/jasig/cas/adaptors/generic/authentication.txt"));
    }

    public void testSupportsProperUserCredentials() {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword("Scott", "rutgers");

        try {
            this.authenticationHandler.authenticate(c);
        } catch (GeneralSecurityException e) {
            fail("AuthenticationException caught.");
        }
    }

    public void testDoesntSupportBadUserCredentials() {
        try {
            final UrlCredential c = new DefaultUrlCredentialImpl(new URL("http://www.rutgers.edu"));
            assertFalse(this.authenticationHandler.supports(c));
        } catch (MalformedURLException e) {
            fail("MalformedURLException caught.");
        }
    }

    public void testAuthenticatesUserInFileWithDefaultSeparator() throws GeneralSecurityException {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword("scott", "rutgers");
        assertTrue(this.authenticationHandler.authenticate(c));
    }

    public void testFailsUserNotInFileWithDefaultSeparator() {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword("fds", "rutgers");

        try {
            assertFalse(this.authenticationHandler.authenticate(c));
        } catch (GeneralSecurityException e) {
            // this is okay because it means the test failed.
        }
    }

    public void testFailsNullUserName() {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword(null, "user");

        try {
            assertFalse(this.authenticationHandler.authenticate(c));
        } catch (GeneralSecurityException e) {
            // this is okay because it means the test failed.
        }
    }

    public void testFailsNullUserNameAndPassword() {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword(null, null);

        try {
            assertFalse(this.authenticationHandler.authenticate(c));
        } catch (GeneralSecurityException e) {
            // this is okay because it means the test failed.
        }
    }

    public void testFailsNullPassword() {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword("scott", null);

        try {
            assertFalse(this.authenticationHandler.authenticate(c));
        } catch (GeneralSecurityException e) {
            // this is okay because it means the test failed.
        }
    }

    public void testAuthenticatesUserInFileWithCommaSeparator() throws GeneralSecurityException {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword("scott", "rutgers");

        this.authenticationHandler.setFileName(new ClassPathResource("org/jasig/cas/adaptors/generic/authentication2.txt"));
        this.authenticationHandler.setSeparator(",");
        assertTrue(this.authenticationHandler.authenticate(c));
    }

    public void testFailsUserNotInFileWithCommaSeparator() {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword("fds", "rutgers");

        this.authenticationHandler.setFileName(new ClassPathResource("org/jasig/cas/adaptors/generic/authentication2.txt"));
        this.authenticationHandler.setSeparator(",");

        try {
            assertFalse(this.authenticationHandler.authenticate(c));
        } catch (GeneralSecurityException e) {
            // this is okay because it means the test failed.
        }
    }

    public void testFailsGoodUsernameBadPassword() {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword("scott", "rutgers1");

        this.authenticationHandler.setFileName(new ClassPathResource("org/jasig/cas/adaptors/generic/authentication2.txt"));
        this.authenticationHandler.setSeparator(",");

        try {
            assertFalse(this.authenticationHandler.authenticate(c));
        } catch (GeneralSecurityException e) {
            // this is okay because it means the test failed.
        }
    }

    public void testAuthenticateNoFileName() {
        final UserNamePasswordCredential c = TestUtils.getCredentialsWithDifferentUsernameAndPassword("scott", "rutgers");
        this.authenticationHandler.setFileName(new ClassPathResource("fff"));

        try {
            assertFalse(this.authenticationHandler.authenticate(c));
        } catch (Exception e) {
            // this is good
        }
    }
}