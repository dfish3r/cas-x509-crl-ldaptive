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
package org.jasig.cas.server.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;

/**
 * Abstract authentication handler that handles the automatic naming of authentication handlers.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractNamedAuthenticationHandler implements AuthenticationHandler {
    
    /** Instance of logging for subclasses. */
    protected Logger log = LoggerFactory.getLogger(this.getClass());
    
    /** The name of the authentication handler. */
    @NotNull
    private String name = getClass().getName();

    public final void setName(final String name) {
        this.name = name;
    }
    
    public final String getName() {
        return this.name;
    }
}
