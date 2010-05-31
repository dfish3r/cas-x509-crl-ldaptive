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

package org.jasig.cas.web.view;

import java.util.*;

import junit.framework.TestCase;

import org.jasig.cas.TestUtils;
import org.jasig.cas.server.authentication.AttributePrincipal;
import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.session.Assertion;
import org.jasig.cas.validation.ImmutableAssertionImpl;
import org.jasig.cas.web.view.Cas10ResponseViewTests.MockWriterHttpMockHttpServletResponse;
import org.opensaml.SAMLAuthenticationStatement;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1
 *
 */
public class Saml10SuccessResponseViewTests extends TestCase {

    private Saml10SuccessResponseView response;

    private final Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();

    private final AttributePrincipal principal = new AttributePrincipal() {
        public List<Object> getAttributeValues(final String attribute) {
            return attributes.get(attribute);
        }

        public Object getAttributeValue(final String attribute) {
            final List<Object> attributes = getAttributeValues(attribute);
            if (attributes != null && !attributes.isEmpty()) {
                return attributes.get(0);
            }

            return null;
        }

        public Map<String, List<Object>> getAttributes() {
            return attributes;
        }

        public String getName() {
            return "testPrincipal";
        }
    };

    private Authentication authentication = new Authentication() {

        private final Date date = new Date();

        private final Map<String, List<Object>> maps = new HashMap<String, List<Object>>();

        public Date getAuthenticationDate() {
            return date;
        }

        public Map<String, List<Object>> getAuthenticationMetaData() {
            return maps;
        }

        public boolean isLongTermAuthentication() {
            return false;
        }

        public String getAuthenticationMethod() {
            return "urn:ietf:rfc:2246";
        }
    };

    public Saml10SuccessResponseViewTests() {
        attributes.put("testAttribute", Arrays.asList((Object) "testValue"));
        attributes.put("testEmptyCollection", Collections.emptyList());
        attributes.put("testAttributeCollection", Arrays.asList((Object) "tac1", "tac2"));
    }
    
    protected void setUp() throws Exception {
        this.response = new Saml10SuccessResponseView();
        this.response.setIssuer("testIssuer");
        this.response.setIssueLength(1000);
        super.setUp();
    }

    public void testResponse() throws Exception {
        final Map<String, Object> model = new HashMap<String, Object>();
        final List<Authentication> authentications = new ArrayList<Authentication>();
        authentications.add(authentication);
        
        final Assertion assertion = new ImmutableAssertionImpl(authentications, TestUtils.getService(), true);
        
        model.put("assertion", assertion);
        
        final MockWriterHttpMockHttpServletResponse servletResponse = new MockWriterHttpMockHttpServletResponse();
        
        this.response.renderMergedOutputModel(model, new MockHttpServletRequest(), servletResponse);
        final String written = servletResponse.getWrittenValue();
        
        assertTrue(written.contains("testPrincipal"));
        assertTrue(written.contains("testAttribute"));
        assertTrue(written.contains("testValue"));
        assertFalse(written.contains("testEmptyCollection"));
        assertTrue(written.contains("testAttributeCollection"));
        assertTrue(written.contains("tac1"));
        assertTrue(written.contains("tac2"));
        assertTrue(written.contains(SAMLAuthenticationStatement.AuthenticationMethod_SSL_TLS_Client));
        assertTrue(written.contains("AuthenticationMethod"));
    }
    
    public void testResponseWithNoAttributes() throws Exception {
        final Map<String, Object> model = new HashMap<String, Object>();

        authentication.getAuthenticationMetaData().put("testSamlAttribute", Arrays.asList((Object) "value"));
        
        final List<Authentication> authentications = new ArrayList<Authentication>();
        authentications.add(authentication);
        
        final Assertion assertion = new ImmutableAssertionImpl(authentications, TestUtils.getService(), true);
        
        model.put("assertion", assertion);
        
        final MockWriterHttpMockHttpServletResponse servletResponse = new MockWriterHttpMockHttpServletResponse();
        
        this.response.renderMergedOutputModel(model, new MockHttpServletRequest(), servletResponse);
        final String written = servletResponse.getWrittenValue();
        
        assertTrue(written.contains("testPrincipal"));
        assertTrue(written.contains(SAMLAuthenticationStatement.AuthenticationMethod_SSL_TLS_Client));
        assertTrue(written.contains("AuthenticationMethod"));
    }
    
    public void testResponseWithoutAuthMethod() throws Exception {
        final Map<String, Object> model = new HashMap<String, Object>();
                
        final List<Authentication> authentications = new ArrayList<Authentication>();
        authentications.add(authentication);
        
        final Assertion assertion = new ImmutableAssertionImpl(authentications, TestUtils.getService(), true);
        
        model.put("assertion", assertion);
        
        final MockWriterHttpMockHttpServletResponse servletResponse = new MockWriterHttpMockHttpServletResponse();
        
        this.response.renderMergedOutputModel(model, new MockHttpServletRequest(), servletResponse);
        final String written = servletResponse.getWrittenValue();
        
        assertTrue(written.contains("testPrincipal"));
        assertTrue(written.contains("testAttribute"));
        assertTrue(written.contains("testValue"));
        assertTrue(written.contains("urn:oasis:names:tc:SAML:1.0:am:unspecified"));       
    }
    
    public void testException() {
        this.response.setIssuer(null);
        
        final Map<String, Object> model = new HashMap<String, Object>();

        final List<Authentication> authentications = new ArrayList<Authentication>();
        authentications.add(authentication);
        
        final Assertion assertion = new ImmutableAssertionImpl(authentications, TestUtils.getService(), true);
        
        model.put("assertion", assertion);
        
        try {
            this.response.renderMergedOutputModel(model, new MockHttpServletRequest(), new MockHttpServletResponse());
            fail("Exception expected.");
        } catch (final Exception e) {
            return;
        }
    }
}
