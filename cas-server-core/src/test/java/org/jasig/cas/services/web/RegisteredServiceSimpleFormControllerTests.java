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

package org.jasig.cas.services.web;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jasig.cas.server.session.RegisteredService;
import org.jasig.cas.server.session.ServicesManager;
import org.jasig.cas.services.DefaultServicesManagerImpl;
import org.jasig.cas.services.InMemoryServiceRegistryDaoImpl;
import org.jasig.cas.services.RegisteredServiceImpl;
import org.jasig.cas.services.web.support.RegisteredServiceValidator;
import org.jasig.services.persondir.support.StubPersonAttributeDao;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;

import junit.framework.TestCase;

/**
 * @author Scott Battaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1
 */
public class RegisteredServiceSimpleFormControllerTests extends TestCase {

    private RegisteredServiceSimpleFormController controller;

    private ServicesManager manager;

    private StubPersonAttributeDao repository;

    @Override
    protected void setUp() throws Exception {
        final Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();
        attributes.put("test", Arrays.asList(new Object[] {"test"}));

        this.repository = new StubPersonAttributeDao();
        this.repository.setBackingMap(attributes);

        this.manager = new DefaultServicesManagerImpl(
            new InMemoryServiceRegistryDaoImpl());
        
        final RegisteredServiceValidator validator = new RegisteredServiceValidator();
        validator.setServicesManager(this.manager);

        this.controller = new RegisteredServiceSimpleFormController(
            this.manager, this.repository);
        this.controller.setCommandClass(RegisteredServiceImpl.class);
        this.controller.setCommandName("registeredService");
        this.controller.setValidator(validator);
    }

    public void testAddRegisteredServiceNoValues() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        request.setMethod("POST");

        final ModelAndView modelAndView = this.controller.handleRequest(
            request, response);

        final BindingResult result = (BindingResult) modelAndView
            .getModel()
            .get(
                "org.springframework.validation.BindingResult.registeredService");

        assertTrue(result.hasErrors());
    }

    public void testAddRegisteredServiceWithValues() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();

        request.addParameter("description", "description");
        request.addParameter("serviceId", "serviceId");
        request.addParameter("name", "name");
        request.addParameter("theme", "theme");
        request.addParameter("allowedToProxy", "true");
        request.addParameter("enabled", "true");
        request.addParameter("ssoEnabled", "true");
        request.addParameter("anonymousAccess", "false");
        request.addParameter("evaluationOrder", "1");

        request.setMethod("POST");

        assertTrue(this.manager.getAllServices().isEmpty());

        this.controller.handleRequest(
            request, response);

        assertFalse(this.manager.getAllServices().isEmpty());
    }
    
    public void testEditRegisteredServiceWithValues() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final MockHttpServletResponse response = new MockHttpServletResponse();
        
        final RegisteredServiceImpl r = new RegisteredServiceImpl();
        r.setId(1000);
        r.setServiceId("test");
        r.setDescription("description");
        
        this.manager.save(r);

        request.addParameter("description", "description");
        request.addParameter("serviceId", "serviceId1");
        request.addParameter("name", "name");
        request.addParameter("theme", "theme");
        request.addParameter("allowedToProxy", "true");
        request.addParameter("enabled", "true");
        request.addParameter("ssoEnabled", "true");
        request.addParameter("anonymousAccess", "false");
        request.addParameter("evaluationOrder", "2");
        request.addParameter("id", "1000");

        request.setMethod("POST");

        this.controller.handleRequest(
            request, response);

        assertFalse(this.manager.getAllServices().isEmpty());
        final RegisteredService r2 = this.manager.findServiceBy(1000);
        
        assertEquals("serviceId1", r2.getServiceId());
    }
}
