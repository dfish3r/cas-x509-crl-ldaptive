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

package org.jasig.cas.server.session;

import org.jasig.cas.server.Saml11Profile;
import org.jasig.cas.server.authentication.AttributePrincipal;
import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.login.Saml1TokenServiceAccessRequestImpl;
import org.jasig.cas.server.util.DefaultServiceIdentifierMatcherImpl;
import org.jasig.cas.server.util.ServiceIdentifierMatcher;
import org.junit.Test;
import org.opensaml.DefaultBootstrap;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.util.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class Saml1ProtocolAccessImplTests {

    @Test
    public void browserPost() throws Exception {
        DefaultBootstrap.bootstrap();
        final SamlTest samlTest = new SamlTest(Saml11Profile.BrowserPost);
        final AccessResponseResult result = samlTest.generateResponse(new DefaultAccessResponseRequestImpl(new StringWriter()));
        assertEquals(2, result.getParameters().size());
    }

    @Test
    public void browserArtifact() throws Exception {
        DefaultBootstrap.bootstrap();
        final SamlTest samlTest = new SamlTest(Saml11Profile.BrowserArtifact);
        final AccessResponseResult result = samlTest.generateResponse(new DefaultAccessResponseRequestImpl(new StringWriter()));
        assertEquals(2, result.getParameters().size());
        assertEquals(samlTest.getId(), result.getParameters().get("SAMLart").get(0));
        assertEquals(samlTest.getResourceIdentifier(), result.getParameters().get("TARGET").get(0));
    }

    @Test
    public void validate() throws Exception {
        DefaultBootstrap.bootstrap();
        final String xml = "<samlp:Request xmlns:samlp=\"urn:oasis:names:tc:SAML:1.0:protocol\" MajorVersion=\"1\" MinorVersion=\"1\" RequestID=\"_192.168.16.51.1024506224022\" IssueInstant=\"2002-06-19T17:03:44.022Z\">\n" +
                "       <samlp:AssertionArtifact>\n" +
                "         ArtifactId\n" +
                "       </samlp:AssertionArtifact>\n" +
                "     </samlp:Request>";


        final Saml1TokenServiceAccessRequestImpl request = new Saml1TokenServiceAccessRequestImpl(xml, "127.0.0.1");
        final SamlTest samlTest = new SamlTest(Saml11Profile.BrowserArtifact);
        samlTest.validate(request);
        final StringWriter writer = new StringWriter();
        final AccessResponseResult result = samlTest.generateResponse(new DefaultAccessResponseRequestImpl(writer));
        assertNotNull(writer.getBuffer().toString());
        assertTrue(writer.getBuffer().toString().length() > 0);
        assertTrue(writer.getBuffer().toString().contains("scott"));
    }


    private class SamlTest extends AbstractSaml1ProtocolAccessImpl {

        private Saml11Profile saml11Profile;

        private String requestId;

        private ValidationStatus validationStatus = ValidationStatus.NOT_VALIDATED;

        public SamlTest(final Saml11Profile saml11Profile) {
            this.saml11Profile = saml11Profile;
        }

        @Override
        protected ValidationStatus getValidationStatus() {
            return this.validationStatus;
        }

        @Override
        protected void setValidationStatus(final ValidationStatus validationStatus) {
            this.validationStatus = validationStatus;
        }

        @Override
        protected Saml11Profile getProfile() {
            return this.saml11Profile;
        }

        @Override
        protected Session getParentSession() {
            final Authentication authentication = mock(Authentication.class);
            when(authentication.getAuthenticationMethod()).thenReturn("test");
            when(authentication.getAuthenticationDate()).thenReturn(new Date());
            when(authentication.getAuthenticationMetaData()).thenReturn(Collections.<String, List<Object>>emptyMap());
            final AttributePrincipal attributePrincipal = mock(AttributePrincipal.class);
            final Session session = mock(Session.class);
            when(session.getRootPrincipal()).thenReturn(attributePrincipal);
            when(session.getRootAuthentications()).thenReturn(new HashSet<Authentication>(Arrays.asList(authentication)));
            when(attributePrincipal.getName()).thenReturn("scott");
            return session;
        }

        @Override
        protected String getIssuer() {
            return "foobar";
        }

        @Override
        protected long getIssueLength() {
            return 1000;
        }

        @Override
        protected void setRequestId(final String requestId) {
            this.requestId = requestId;
        }

        @Override
        protected String getRequestId() {
            return this.requestId;
        }

        public String getId() {
            return "ArtifactId";
        }

        public String getResourceIdentifier() {
            return "http://www.cnn.com";
        }

        public Session createDelegatedSession(AuthenticationResponse authenticationResponse) throws InvalidatedSessionException {
            throw new UnsupportedOperationException();
        }
    }
}
