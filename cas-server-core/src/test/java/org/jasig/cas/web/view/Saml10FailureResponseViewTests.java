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

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Collections;

import org.springframework.mock.web.MockHttpServletRequest;

import junit.framework.TestCase;
import org.springframework.mock.web.MockHttpServletResponse;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1
 *
 */
public class Saml10FailureResponseViewTests extends TestCase {

    private Saml10FailureResponseView view = new Saml10FailureResponseView();
    
    public void testResponse() throws Exception {
        final MockHttpServletRequest request =  new MockHttpServletRequest();
        final MockWriterHttpMockHttpServletResponse response = new MockWriterHttpMockHttpServletResponse();
        request.addParameter("TARGET", "service");
        
        final String description = "Validation failed";
        this.view.renderMergedOutputModel(
            Collections.singletonMap("description", description), request, response);
        
        final String responseText = response.getWrittenValue();
        assertTrue(responseText.contains("Status"));
        assertTrue(responseText.contains(description));
    }

    protected static class MockWriterHttpMockHttpServletResponse extends
            MockHttpServletResponse {

        private StringBuilder builder = new StringBuilder();

        public PrintWriter getWriter() {
            try {
                return new MockPrintWriter(new ByteArrayOutputStream(), this.builder);
            } catch (Exception e) {
                throw new RuntimeException();
            }
        }

        public String getWrittenValue() {
            return this.builder.toString();
        }
    }

    protected static class MockPrintWriter extends PrintWriter {

        final StringBuilder builder;

        public MockPrintWriter(OutputStream out, boolean autoFlush, final StringBuilder builder) {
            super(out, autoFlush);
            this.builder = builder;
        }

        public MockPrintWriter(OutputStream out, final StringBuilder builder) {
            super(out);
            this.builder = builder;
        }

        public MockPrintWriter(Writer out, boolean autoFlush, final StringBuilder builder) {
            super(out, autoFlush);
            this.builder = builder;
        }

        public MockPrintWriter(Writer out, final StringBuilder builder) {
            super(out);
            this.builder = builder;
        }

        public void print(String s) {
            this.builder.append(s);
        }
    }
    
}
