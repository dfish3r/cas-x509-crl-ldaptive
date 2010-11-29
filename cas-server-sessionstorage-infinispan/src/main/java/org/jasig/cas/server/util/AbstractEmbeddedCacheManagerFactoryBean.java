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
 * Created by IntelliJ IDEA.
 * User: scottbattaglia
 * Date: 11/28/10
 * Time: 10:10 PM
 * To change this template use File | Settings | File Templates.
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
