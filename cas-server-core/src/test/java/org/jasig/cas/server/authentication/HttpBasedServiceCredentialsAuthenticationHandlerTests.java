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

import org.jasig.cas.TestUtils;
import org.jasig.cas.server.authentication.UrlCredentialAuthenticationHandler;
import org.jasig.cas.util.HttpClient;

import junit.framework.TestCase;

import java.security.GeneralSecurityException;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class HttpBasedServiceCredentialsAuthenticationHandlerTests extends TestCase {

    private UrlCredentialAuthenticationHandler authenticationHandler;

    protected void setUp() throws Exception {
        this.authenticationHandler = new UrlCredentialAuthenticationHandler(new HttpClient());
    }

    public void testSupportsProperUserCredentials() {
        assertTrue(this.authenticationHandler.supports(TestUtils
            .getHttpBasedServiceCredentials()));
    }

    public void testDoesntSupportBadUserCredentials() {
        assertFalse(this.authenticationHandler.supports(TestUtils
            .getCredentialsWithSameUsernameAndPassword()));
    }

    public void testAcceptsProperCertificateCredentials() throws GeneralSecurityException {
        this.authenticationHandler.authenticate(TestUtils.getHttpBasedServiceCredentials());
    }

    public void testRejectsInProperCertificateCredentials() throws GeneralSecurityException {
        try {
            this.authenticationHandler.authenticate(TestUtils.getHttpBasedServiceCredentials("https://clearinghouse.ja-sig.org"));
            fail();
        } catch (final GeneralSecurityException e) {
            // this is okay
        }
    }

    public void testRejectsNonHttpsCredentials() throws GeneralSecurityException {
        try {
            this.authenticationHandler.authenticate(TestUtils.getHttpBasedServiceCredentials("http://www.jasig.org"));
            fail();
        } catch (final GeneralSecurityException e) {
          // okay
        }
    }
    
    public void testAcceptsNonHttpsCredentials() throws GeneralSecurityException {
        this.authenticationHandler.setRequireSecure(false);
        this.authenticationHandler.authenticate(TestUtils.getHttpBasedServiceCredentials("http://www.ja-sig.org"));
    }

    public void testNoAcceptableStatusCode() throws Exception {
        try {
            this.authenticationHandler.authenticate(TestUtils.getHttpBasedServiceCredentials("https://clue.acs.rutgers.edu"));
            fail();
        } catch (final Exception e) {
            // this is okay
        }
    }
    
    public void testNoAcceptableStatusCodeButOneSet() throws Exception {
        final HttpClient httpClient = new HttpClient();
        httpClient.setAcceptableCodes(new int[] {900});
        this.authenticationHandler = new UrlCredentialAuthenticationHandler(httpClient);
        try {
            this.authenticationHandler.authenticate(TestUtils.getHttpBasedServiceCredentials("https://www.ja-sig.org"));
            fail();
        } catch (final GeneralSecurityException e) {
            // this is okay
        }
    }
}