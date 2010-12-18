package org.jasig.cas.server;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Ehcache;
import org.jasig.cas.server.authentication.AttributePrincipalFactory;
import org.jasig.cas.server.authentication.AuthenticationFactory;
import org.jasig.cas.server.authentication.SerializableAttributePrincipalFactoryImpl;
import org.jasig.cas.server.authentication.SerializableAuthenticationFactoryImpl;
import org.jasig.cas.server.session.AbstractSessionStorageTests;
import org.jasig.cas.server.session.AccessFactory;
import org.jasig.cas.server.session.SessionStorage;
import org.junit.After;
import org.junit.Before;

import java.util.Collections;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class EhcacheSessionStorageImplTests extends AbstractSessionStorageTests {

    public EhcacheSessionStorageImplTests() {
        final CacheManager cacheManager = CacheManager.getInstance();
        final Ehcache cache = new Cache("cache", 1000, false, false, 5000, 5000);
        final Ehcache principalMapping = new Cache("principalMapping", 1000, false, false, 5000, 5000);
        final Ehcache cacheMappings = new Cache("cacheMappings", 1000, false, false, 5000, 5000);

        try {
            cacheManager.addCache(cache);
            cacheManager.addCache(principalMapping);
            cacheManager.addCache(cacheMappings);
        } catch (final Exception e) {
            // ignore these.  We're correcting for some wonky @Before behavior that's not having the local @Before methods get executed.
        }
    }

    @Override
    protected SessionStorage getSessionStorage() {
        final CacheManager cacheManager = CacheManager.getInstance();
        final Ehcache cache = cacheManager.getCache("cache");
        final Ehcache cacheMappings = cacheManager.getCache("cacheMappings");
        final Ehcache principalMapping = cacheManager.getCache("principalMapping");

        cache.removeAll();
        cacheMappings.removeAll();
        principalMapping.removeAll();

        return new EhcacheSessionStorageImpl(cache, cacheMappings, principalMapping, Collections.<AccessFactory>emptyList(), null);
    }

   @Override
    protected AuthenticationFactory getAuthenticationFactory() {
        return new SerializableAuthenticationFactoryImpl();
    }

    @Override
    protected AttributePrincipalFactory getAttributePrincipalFactory() {
        return new SerializableAttributePrincipalFactoryImpl();
    }
}
