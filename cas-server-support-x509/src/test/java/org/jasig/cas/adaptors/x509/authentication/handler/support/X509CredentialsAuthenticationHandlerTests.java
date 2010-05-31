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

package org.jasig.cas.adaptors.x509.authentication.handler.support;

import java.security.cert.X509Certificate;

import org.jasig.cas.adaptors.x509.authentication.principal.AbstractX509CertificateTests;
import org.jasig.cas.adaptors.x509.authentication.principal.X509CertificateCredentials;
import org.jasig.cas.server.authentication.DefaultUserNamePasswordCredential;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.4
 *
 */
public class X509CredentialsAuthenticationHandlerTests extends AbstractX509CertificateTests {
    private X509CredentialsAuthenticationHandler authenticationHandler;

    protected void setUp() throws Exception {
        this.authenticationHandler = new X509CredentialsAuthenticationHandler();
        this.authenticationHandler.setTrustedIssuerDnPattern("JA-SIG");       
    }
    
    public void testSupportsClass() {
        assertTrue(this.authenticationHandler.supports(new X509CertificateCredentials(new X509Certificate[0])));
    }
    
    public void testDoesntSupportClass() {
        assertFalse(this.authenticationHandler.supports(new DefaultUserNamePasswordCredential()));
    }
    
    public void testInvalidCertificate() throws Exception {
        final X509CertificateCredentials credentials = new X509CertificateCredentials(new X509Certificate[] {INVALID_CERTIFICATE});
        
        assertFalse(this.authenticationHandler.authenticate(credentials));
    }
    
    public void testValidCertificate() throws Exception {
        final X509CertificateCredentials credentials = new X509CertificateCredentials(new X509Certificate[] {VALID_CERTIFICATE});
        
        assertTrue(this.authenticationHandler.authenticate(credentials));
    }
    
    public void testValidCertificateWithInvalidFirst() throws Exception {
        final X509CertificateCredentials credentials = new X509CertificateCredentials(new X509Certificate[] {INVALID_CERTIFICATE, VALID_CERTIFICATE});
        
        assertFalse(this.authenticationHandler.authenticate(credentials));
    }
    
    public void testValidCertificateWithNotTrustedIssuer() throws Exception {
        this.authenticationHandler.setTrustedIssuerDnPattern("test");
        final X509CertificateCredentials credentials = new X509CertificateCredentials(new X509Certificate[] {VALID_CERTIFICATE});
        
        assertFalse(this.authenticationHandler.authenticate(credentials));
    }
    
    public void testValidCertificateWithCustomDistinguishedDn() throws Exception {
        this.authenticationHandler.setSubjectDnPattern("s.*");
        final X509CertificateCredentials credentials = new X509CertificateCredentials(new X509Certificate[] {VALID_CERTIFICATE});
        
        assertTrue(this.authenticationHandler.authenticate(credentials));
    }
}