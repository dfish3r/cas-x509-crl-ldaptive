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

package org.jasig.cas.server.services;


import org.jasig.cas.server.session.RegisteredService;
import org.jasig.cas.server.session.ServicesManager;
import org.jasig.cas.services.RegisteredServiceImpl;
import org.jasig.services.persondir.IPersonAttributeDao;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindException;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.SimpleFormController;
import org.springframework.web.servlet.view.RedirectView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

/**
 * SimpleFormController to handle adding/editing of RegisteredServices.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
@Controller
@RequestMapping(value = {"/services/edit.html", "/services/add.html"})
public final class RegisteredServiceSimpleFormController extends SimpleFormController {

    /** Instance of ServiceRegistryManager */
    @NotNull
    private final ServicesManager servicesManager;

    /** Instance of AttributeRegistry. */
    @NotNull
    private final IPersonAttributeDao personAttributeDao;

    @Inject
    public RegisteredServiceSimpleFormController(final ServicesManager servicesManager, final IPersonAttributeDao attributeRepository) {
        this.servicesManager = servicesManager;
        this.personAttributeDao = attributeRepository;
        setCommandName("registeredService");
        setSuccessView("addServiceView");
        setFormView("addServiceView");
        setValidator(new RegisteredServiceValidator(servicesManager));
    }

    /**
     * Sets the require fields and the disallowed fields from the
     * HttpServletRequest.
     * 
     * @see org.springframework.web.servlet.mvc.BaseCommandController#initBinder(javax.servlet.http.HttpServletRequest,
     * org.springframework.web.bind.ServletRequestDataBinder)
     */
    protected final void initBinder(final HttpServletRequest request,
        final ServletRequestDataBinder binder) throws Exception {
        binder.setRequiredFields("description", "serviceId",
            "name", "allowedToProxy", "enabled", "ssoEnabled",
            "anonymousAccess", "evaluationOrder");
        binder.setDisallowedFields("id");
        binder.registerCustomEditor(String.class, new StringTrimmerEditor(true));
    }

    /**
     * Adds the service to the ServiceRegistry via the ServiceRegistryManager.
     * 
     * @see org.springframework.web.servlet.mvc.SimpleFormController#onSubmit(javax.servlet.http.HttpServletRequest,
     * javax.servlet.http.HttpServletResponse, java.lang.Object,
     * org.springframework.validation.BindException)
     */
    protected final ModelAndView onSubmit(final HttpServletRequest request,
        final HttpServletResponse response, final Object command,
        final BindException errors) throws Exception {
        final RegisteredService service = (RegisteredService) command;

        this.servicesManager.save(service);
        logger.info("Saved changes to service " + service.getId());

        final ModelAndView modelAndView = new ModelAndView(new RedirectView(
            "/services/manage.html#" + service.getId(), true));
        modelAndView.addObject("action", "add");
        modelAndView.addObject("id", service.getId());

        return modelAndView;
    }

    protected Object formBackingObject(final HttpServletRequest request)
        throws Exception {
        final String id = request.getParameter("id");

        if (!StringUtils.hasText(id)) {
            logger.debug("Created new service.");
            return new RegisteredServiceImpl();
        }
        
        final RegisteredService service = this.servicesManager.findServiceBy(Long.parseLong(id));
        
        if (service != null) {
            logger.debug("Loaded service " + service.getServiceId());
        } else {
            logger.debug("Invalid service id specified.");
        }

        return service;
    }

    /**
     * Returns the attributes, page title, and command name.
     * 
     * @see org.springframework.web.servlet.mvc.SimpleFormController#referenceData(javax.servlet.http.HttpServletRequest)
     */
    protected final Map referenceData(final HttpServletRequest request)
        throws Exception {
        final Map<String, Object> model = new HashMap<String, Object>();
        model
            .put("availableAttributes", this.personAttributeDao.getPossibleUserAttributeNames());
        model.put("pageTitle", getFormView());
        model.put("commandName", getCommandName());
        return model;
    }
}
