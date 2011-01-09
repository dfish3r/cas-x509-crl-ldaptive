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

package org.jasig.cas.server.util;

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;

/**
 * Abstract class to support Infinispan Cache.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public abstract class AbstractEmbeddedCacheManagerFactoryBean implements FactoryBean<Cache>, InitializingBean, DisposableBean {

    @NotNull
    private Resource resource;

    private EmbeddedCacheManager cacheManager;

    protected final EmbeddedCacheManager getCacheManager() {
        return this.cacheManager;
    }

    public final Class<?> getObjectType() {
        return Cache.class;
    }

    public final void setConfiguration(final Resource resource) {
        this.resource = resource;
    }

    public final void afterPropertiesSet() throws Exception {
        this.cacheManager = new DefaultCacheManager(resource.getInputStream(), true);
    }

    public final void destroy() throws Exception {
        this.cacheManager.stop();
    }

}
