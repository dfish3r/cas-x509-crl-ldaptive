package org.jasig.cas.server.util;

import org.infinispan.Cache;

import javax.validation.constraints.NotNull;

/**
 * Returns a Cache based on the name provided.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class NamedInfinispanCacheFactoryBean extends AbstractEmbeddedCacheManagerFactoryBean {

    @NotNull
    private String name;

    public Cache getObject() throws Exception {
        return getCacheManager().getCache(this.name);
    }

    public boolean isSingleton() {
        return false;
    }

    public void setName(final String name) {
        this.name = name;
    }
}
