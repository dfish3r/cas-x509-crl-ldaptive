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
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * RegisteredServiceValidator ensures that a new RegisteredService does not have
 * a conflicting Service Id with another service already in the registry.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public final class RegisteredServiceValidator implements Validator {

    /** Default length, which matches what is in the view. */
    private static final int DEFAULT_MAX_DESCRIPTION_LENGTH = 300;

    /** ServiceRegistry to look up services. */
    @NotNull
    private final ServicesManager servicesManager;

    /** The maximum length of the description we will accept. */
    @Min(0)
    private int maxDescriptionLength = DEFAULT_MAX_DESCRIPTION_LENGTH;

    public RegisteredServiceValidator(final ServicesManager servicesManager) {
        this.servicesManager = servicesManager;
    }

    /**
     * Supports the RegisteredServiceImpl.
     * 
     * @see org.springframework.validation.Validator#supports(java.lang.Class)
     */
    public boolean supports(final Class clazz) {
        return RegisteredServiceImpl.class.equals(clazz);
    }

    public void validate(final Object o, final Errors errors) {
        final RegisteredService r = (RegisteredService) o;

        if (r.getServiceId() != null) {
            for (final RegisteredService service : this.servicesManager
                .getAllServices()) {
                if (r.getServiceId().equals(service.getServiceId())
                    && r.getId() != service.getId()) {
                    errors.rejectValue("serviceId",
                        "registeredService.serviceId.exists", null);
                    break;
                }
            }
        }

        if (r.getDescription() != null
            && r.getDescription().length() > this.maxDescriptionLength) {
            errors.rejectValue("description",
                "registeredService.description.length", null);
        }
    }

    public void setMaxDescriptionLength(final int maxLength) {
        this.maxDescriptionLength = maxLength;
    }
}
