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

package org.jasig.cas.server.login;

import org.apache.commons.codec.binary.Base64;
import org.jasig.cas.server.util.PublicPrivateKeyStore;
import org.jasig.cas.server.util.SamlCompliantThreadLocalDateFormatDateParser;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class Saml2ArtifactRequestAccessRequestImplFactoryTests {

    private final String CONST_SESSION_ID = "session";

    private final String CONST_REMOTE_IP_ADDRESS = "127.0.0.1";

    private final SamlCompliantThreadLocalDateFormatDateParser parser = new SamlCompliantThreadLocalDateFormatDateParser();

    private final String formattedDate = parser.format(new Date());

    private final String SAML_REQUEST_GOOGLE_ACCOUNTS = Base64.encodeBase64String(("<?xml version=\"1.0\" encoding=\"UTF-8\"?><samlp:AuthnRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" ID=\"REQUEST\" Version=\"2.0\" IssueInstant=\"" + formattedDate + "\" ProtocolBinding=\"urn:oasis:names.tc:SAML:2.0:bindings:HTTP-Redirect\" ProviderName=\"google.com\" AssertionConsumerServiceURL=\"https://www.google.com/a/psosamldemo.net/ac\"/>").getBytes());

    private final String SAML_REQUEST_SALESFORCE = Base64.encodeBase64String(("<?xml version=\"1.0\" encoding=\"UTF-8\"?><samlp:AuthnRequest IssueInstant=\"" + formattedDate + "\" AssertionConsumerServiceURL=\"https://login.salesforce.com/?saml=02HKiPoin44ffVaJ8u4mmCw4A5RxpOUG3dKtAOJ4Qm3d2uC6WtvdzzYcU9\" Destination=\"https://yourcasserver.domain:port/cas/login\" ID=\"_2Uug3FPJbyXwe7SCeV5q2g_tFG8ppVAxtHC91g3MgZZcNJT8ZSI9jHUJgxAFT2mcFHIsUBUm4ysGaSScKCqIBmC2kpX2njydy2WdAeEj9bXPCFTMx3lcIrM0ixG.pFgNuq3dv3In_M8d5PZjdJcIolLu12cn6I0lS.Z1eHaYrG8eJvbZUmtdFXFk0shsAtEvCjRpggh1hTJkRx3tizSaktgCmxOAxdg\" ProtocolBinding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST\" Version=\"2.0\" xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\"><saml:Issuer xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">https://saml.salesforce.com</saml:Issuer><ds:Signature xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:SignedInfo xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:CanonicalizationMethod Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"/><ds:SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"/><ds:Reference URI=\"#_2Uug3FPJbyXwe7SCeV5q2g_tFG8qqVAxtHC91g3MgWWcNJT8ZSI8jHUJgxAFT2mcFHIsUBUm4ysGaSScKCqIBmC2kpX2njydy2WdAeEj9bXPCFTMx3lcIrM0ixG.pFgNuq3dv3In_M8d5PZjdJcIolLu12cn6I0lS.Z1eHaYrG7eJvbZUmtdFXFk0shsAtEvCjRpggh1hTJkRx3tizSaktgCmxOAxdg\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:Transforms xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ds:Transform Algorithm=\"http://www.w3.org/2000/09/xmldsig#enveloped-signature\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"/><ds:Transform Algorithm=\"http://www.w3.org/2001/10/xml-exc-c14n#\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"><ec:InclusiveNamespaces PrefixList=\"ds saml samlp\" xmlns:ec=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></ds:Transform></ds:Transforms><ds:DigestMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#sha1\" xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\"/><ds:DigestValue xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">9XIJQ0CMB6qkFa2ktUSwAV+V55g=</ds:DigestValue></ds:Reference></ds:SignedInfo><ds:SignatureValue xmlns:ds=\"http://www.w3.org/2000/09/xmldsig#\">wrEIu6JYesF0MwYUpd68oIVE9bTONkMs3HsQzLj3iRsqupTy1J8eFCwu0YvFMUAz84C/jqnCsZby MGpnTzsi8AcLsgJXN8r/1Btkwpou1UJp8c+Voi5/12erhHoZeA1GWcTyR2F3A7Pcp3LSCZMK6C0b 6usbtbUnsHjrmlwHlnc=</ds:SignatureValue><ds:KeyInfo><ds:X509Data><ds:X509Certificate>MIIESzCCA7SgAwIBAgIQeLtGvqUwAwI+AbaKJ7IFwzANBgkqhkiG9w0BAQUFADCBujEfMB0GA1UE ChMWVmVyaVNpZ24gVHJ1c3QgTmV0d29yazEXMBUGA1UECxMOVmVyaVNpZ24sIEluYy4xMzAxBgNV BAsTKlZlcmlTaWduIEludGVybmF0aW9uYWwgU2VydmVyIENBIC0gQ2xhc3MgMzFJMEcGA1UECxNA d3d3LnZlcmlzaWduLmNvbS9DUFMgSW5jb3JwLmJ5IFJlZi4gTElBQklMSVRZIExURC4oYyk5NyBW ZXJpU2lnbjAeFw0wOTAxMDYwMDAwMDBaFw0xMTAxMDcyMzU5NTlaMIGOMQswCQYDVQQGEwJVUzET MBEGA1UECBMKQ2FsaWZvcm5pYTEWMBQGA1wEBxQNU2FuIEZyYW5jaXNjbzEdMBsGA1UEChQUU2Fs ZXNmb3JjZS5jb20sIEluYy4xFDASBgNVBAsUC0FwcGxpY2F0aW9uMR0wGwYDVQQDFBRwcm94eS5z YWxlc2ZvcmNlLmNvbTCBnzANBgkqhkiG9w0BAQEFCCOBjQAwgYkCgYEAzKElluHQYlUnFm156Nwu p9vqkf9DvnhOJc09VNYKOdz5PkpJ/bFLuN2frmfJTlw6pi4knE2geN3j26iAFGIpqgkfWmAi5knj cIbOvHbMXMg1apuVyK9jmbKy4pITZCj56PtH7qMjlmwN+ZEcQRVy+urRGJRfBEyE+ht5KrewhlcC AwEAAaOCAXowggF2MAkGA1UdEwQCMAAwCwYDVR0PBAQDAgWgMEYGA1UdHwQ/MD0wO6A5oDeGNWh0 dHA6Ly9jcmwudmVyaXNpZ24uY29tL0NsYXNzM0ludGVybmF0aW9uYWxTZXJ2ZXIuY3JsMEQGA1Ud IAQ9MDswOQYLYIZIAYb4RQEHFwMwKjAoBggrBgEFBQcCARYcaHR0cHM6Ly93d3cudmVyaXNpZ24u Y29tL3JwYTAoBgNVHSUEITAfBglghkgBhvhCBAEGCCsGAQUFBwMBBggrBgEFBQcDAjA0BggrBgEF BQcBAQQoMCYwJAYIKwYBBQUHMAGGGGh0dHA6Ly9vY3NwLnZlcmlzaWduLmNvbTBuBggrBgEFBQcB DARiMGChXqBcMFowWDBWFglpbWFnZS9naWYwITAfMAcGBSsOAwIaBBRLa7kolgYMu9BSOJsprEsH iyEFGDAmFiRodHRwOi8vbG9nby52ZXJpc2lnbi5jb20vdnNsb2dvMS5naWYwDQYJKoZIhvcNAQEF BQADgYEAEFpxxlZKNpcHMKpJ8kR28A8MONmZujTSinMeDEBERS9CL6QyK8yIrFr1ofe/qaffFgyC zoZs1MItxybOtSvVTloXLKZ+k6Xb1pLVAphbhaoFf4YV8u+Xn91giot/h9NNkMSu78v7tnQeNfJO VMQB493AaGnbolIYkjTQ0nrU6HE=</ds:X509Certificate></ds:X509Data></ds:KeyInfo></ds:Signature></samlp:AuthnRequest>").getBytes());

    private final String SAML_REQUEST_YAMMER = Base64.encodeBase64String(("<AuthnRequest IssueInstant=\""+ formattedDate + "\" ID=\"uTKS-R0fqjku862q9nY4T6WXVMY\" Version=\"2.0\" xmlns=\"urn:oasis:names:tc:SAML:2.0:protocol\" xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\"><saml:Issuer>yammer.com</saml:Issuer><NameIDPolicy AllowCreate=\"true\"/></AuthnRequest>").getBytes());

    private final PrivateKey CONST_PRIVATE_KEY = mock(PrivateKey.class);

    private final PublicKey CONST_PUBLIC_KEY = mock(PublicKey.class);

    private final PrivateKey CONST_PRIVATE_KEY2 = mock(PrivateKey.class);

    private final PublicKey CONST_PUBLIC_KEY2 = mock(PublicKey.class);

    private final PrivateKey CONST_PRIVATE_KEY_YAMMER = mock(PrivateKey.class);

    private final PublicKey CONST_PUBLIC_KEY_YAMMER = mock(PublicKey.class);

    private Saml2ArtifactRequestAccessRequestImplFactory factory;

    @Before
    public void setUp() throws Exception {
        final Map<String, String> values = new HashMap<String, String>();
        values.put("google.com", "googleAlias");
        values.put("https://saml.salesforce.com", "salesforceAlias");
        values.put("yammer.com", "yammerAlias");

        final PublicPrivateKeyStore keyStore = mock(PublicPrivateKeyStore.class);
        when(keyStore.getPrivateKey("googleAlias")).thenReturn(CONST_PRIVATE_KEY);
        when(keyStore.getPublicKey("googleAlias")).thenReturn(CONST_PUBLIC_KEY);
        when(keyStore.getPrivateKey("salesforceAlias")).thenReturn(CONST_PRIVATE_KEY2);
        when(keyStore.getPublicKey("salesforceAlias")).thenReturn(CONST_PUBLIC_KEY2);
        when(keyStore.getPublicKey("yammerAlias")).thenReturn(CONST_PUBLIC_KEY_YAMMER);
        when(keyStore.getPrivateKey("yammerAlias")).thenReturn(CONST_PRIVATE_KEY_YAMMER);

        this.factory = new Saml2ArtifactRequestAccessRequestImplFactory(keyStore, values);
        final Map<String,String> v = new HashMap<String,String>();
        v.put("yammer.com", "http://www.yammer.com/SAMLEndpoint");
        this.factory.setIssuerToAssertionConsumerUrl(v);
    }

    @Test
    public void googleAccountSupport() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(CONST_REMOTE_IP_ADDRESS);
        request.setParameter("SAMLRequest", SAML_REQUEST_GOOGLE_ACCOUNTS);
        request.setParameter("RelayState", "RELAYSTATE");

        final Saml2ArtifactRequestAccessRequestImpl impl = (Saml2ArtifactRequestAccessRequestImpl) factory.getServiceAccessRequest(CONST_SESSION_ID, CONST_REMOTE_IP_ADDRESS, request.getParameterMap());
        assertNotNull(impl);
        assertEquals(CONST_SESSION_ID, impl.getSessionId());
        assertEquals(CONST_REMOTE_IP_ADDRESS, impl.getRemoteIpAddress());
        assertEquals("https://www.google.com/a/psosamldemo.net/ac", impl.getServiceId());
        assertEquals("REQUEST", impl.getRequestId());
        assertEquals("RELAYSTATE", impl.getRelayState());
        assertEquals(CONST_PUBLIC_KEY, impl.getPublicKey());
        assertEquals(CONST_PRIVATE_KEY, impl.getPrivateKey());
    }

    @Test
    public void salesForceSupport() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(CONST_REMOTE_IP_ADDRESS);
        request.setParameter("SAMLRequest", SAML_REQUEST_SALESFORCE);

        final Saml2ArtifactRequestAccessRequestImpl impl = (Saml2ArtifactRequestAccessRequestImpl) factory.getServiceAccessRequest(CONST_SESSION_ID, CONST_REMOTE_IP_ADDRESS, request.getParameterMap());
        assertNotNull(impl);
        assertEquals(CONST_SESSION_ID, impl.getSessionId());
        assertEquals(CONST_REMOTE_IP_ADDRESS, impl.getRemoteIpAddress());
        assertEquals("https://login.salesforce.com/?saml=02HKiPoin44ffVaJ8u4mmCw4A5RxpOUG3dKtAOJ4Qm3d2uC6WtvdzzYcU9", impl.getServiceId());
        assertEquals("_2Uug3FPJbyXwe7SCeV5q2g_tFG8ppVAxtHC91g3MgZZcNJT8ZSI9jHUJgxAFT2mcFHIsUBUm4ysGaSScKCqIBmC2kpX2njydy2WdAeEj9bXPCFTMx3lcIrM0ixG.pFgNuq3dv3In_M8d5PZjdJcIolLu12cn6I0lS.Z1eHaYrG8eJvbZUmtdFXFk0shsAtEvCjRpggh1hTJkRx3tizSaktgCmxOAxdg", impl.getRequestId());
        assertEquals(CONST_PUBLIC_KEY2, impl.getPublicKey());
        assertEquals(CONST_PRIVATE_KEY2, impl.getPrivateKey());
    }

    @Test
    public void yammerSupport() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr(CONST_REMOTE_IP_ADDRESS);
        request.setParameter("SAMLRequest", SAML_REQUEST_YAMMER);

        final Saml2ArtifactRequestAccessRequestImpl impl = (Saml2ArtifactRequestAccessRequestImpl) factory.getServiceAccessRequest(CONST_SESSION_ID, CONST_REMOTE_IP_ADDRESS, request.getParameterMap());
        assertNotNull(impl);
        assertEquals(CONST_SESSION_ID, impl.getSessionId());
        assertEquals(CONST_REMOTE_IP_ADDRESS, impl.getRemoteIpAddress());
        assertEquals("http://www.yammer.com/SAMLEndpoint", impl.getServiceId());
        assertEquals("uTKS-R0fqjku862q9nY4T6WXVMY", impl.getRequestId());
        assertEquals(CONST_PUBLIC_KEY_YAMMER, impl.getPublicKey());
        assertEquals(CONST_PRIVATE_KEY_YAMMER, impl.getPrivateKey());
    }
}
