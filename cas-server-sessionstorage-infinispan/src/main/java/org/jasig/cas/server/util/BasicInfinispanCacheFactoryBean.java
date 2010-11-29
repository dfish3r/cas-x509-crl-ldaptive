package org.jasig.cas.server.util;

import org.infinispan.Cache;

/**
 * Simple {@link org.springframework.beans.factory.FactoryBean} that just creates caches blindly from the provided
 * configuration file.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class BasicInfinispanCacheFactoryBean extends AbstractEmbeddedCacheManagerFactoryBean {

    public Cache getObject() throws Exception {
        return getCacheManager().getCache();
    }

    public boolean isSingleton() {
        return false;
    }
}
