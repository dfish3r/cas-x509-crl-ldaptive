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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jasig.cas.server.session.RegisteredService;
import org.jasig.cas.server.session.ServicesManager;
import org.springframework.beans.support.PropertyComparator;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

/**
 * MultiActionController to handle the deletion of RegisteredServices as well as
 * displaying them on the Manage Services page.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
@Controller
public final class ManageRegisteredServicesMultiActionController {

    /** View name for the Manage Services View. */
    private static final String VIEW_NAME = "org.jasig.cas.services.manage";

    /** Instance of ServicesManager. */
    @NotNull
    private final ServicesManager servicesManager;

    /** Used to ensure services are sorted by name. */
    private final PropertyComparator propertyComparator = new PropertyComparator("name", false, true);

    @NotNull
    private final String defaultServiceUrl;

    /**
     * Constructor that takes the required {@link ServicesManager}.
     * 
     * @param servicesManager the Services Manager that manages the
     * RegisteredServices.
     * @param serviceProperties the service management tool's url.
     */
    @Inject
    public ManageRegisteredServicesMultiActionController(final ServicesManager servicesManager, final ServiceProperties serviceProperties) {
        this.servicesManager = servicesManager;
        this.defaultServiceUrl = serviceProperties.getService();
    }

    /**
     * Method to delete the RegisteredService by its ID.
     * 
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @return the Model and View to go to after the service is deleted.
     */
    @RequestMapping(method=RequestMethod.GET, value = "/services/delete.html")
    public ModelAndView deleteRegisteredService(final HttpServletRequest request, final HttpServletResponse response) {
        final String id = request.getParameter("id");
        final long idAsLong = Long.parseLong(id);

        final ModelAndView modelAndView = new ModelAndView(new RedirectView("/services/manage.html", true), "status", "deleted");

        final RegisteredService r = this.servicesManager.delete(idAsLong);

        modelAndView.addObject("serviceName", r != null ? r.getName() : "");

        return modelAndView;
    }

    /**
     * Method to show the RegisteredServices.
     * 
     * @param request the HttpServletRequest
     * @param response the HttpServletResponse
     * @return the Model and View to go to after the services are loaded.
     */
    @RequestMapping(method = RequestMethod.GET, value = "/services/manage.html")
    public ModelAndView manage(final HttpServletRequest request, final HttpServletResponse response) {
        final Map<String, Object> model = new HashMap<String, Object>();

        final List<RegisteredService> services = new ArrayList<RegisteredService>(this.servicesManager.getAllServices());
        PropertyComparator.sort(services, this.propertyComparator.getSortDefinition());

        model.put("services", services);
        model.put("pageTitle", VIEW_NAME);
        model.put("defaultServiceUrl", this.defaultServiceUrl);

        return new ModelAndView(VIEW_NAME, model);
    }
}
