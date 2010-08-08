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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * Abstract class to hold most of the items that are needed by implementations to construct a new
 * session.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSessionStorage implements SessionStorage {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @NotNull
    @Size(min=1)
    private final List<AccessFactory> accessFactories;

    @NotNull
    private ExpirationPolicy expirationPolicy = new MultiUseOrTimeToLiveExpirationPolicy(21600);

    @NotNull
    private ServicesManager servicesManager;

    protected AbstractSessionStorage(final List<AccessFactory> accessFactories, final ServicesManager servicesManager) {
        this.accessFactories = accessFactories;
        this.servicesManager = servicesManager;
    }

    public final void setExpirationPolicy(final ExpirationPolicy expirationPolicy) {
        this.expirationPolicy = expirationPolicy;
    }

    protected final ExpirationPolicy getExpirationPolicy() {
        return this.expirationPolicy;
    }

    protected final List<AccessFactory> getAccessFactories() {
        return this.accessFactories;
    }

    protected final ServicesManager getServicesManager() {
        return this.servicesManager;
    }
}
