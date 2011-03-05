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

package org.jasig.cas.server.web;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class ValidationControllerTests {

    private ValidationController validationController;

    public void cas10Protocol() throws IOException {
        final MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        final StringWriter writer = new StringWriter();
        final ModelAndView modelAndView = this.validationController.validateCas10Request(true, "http://www.cnn.com", "ST-100", mockHttpServletRequest, writer);
        assertNull(modelAndView);
    }

    @Test
    public void cas20Protocol() {

    }

    public void cas20ProtocolWithPGT() {

    }



}
