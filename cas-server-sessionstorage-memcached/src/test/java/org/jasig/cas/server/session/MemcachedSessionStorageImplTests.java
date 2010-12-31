package org.jasig.cas.server.session;

import com.thimbleware.jmemcached.CacheImpl;
import com.thimbleware.jmemcached.MemCacheDaemon;
import com.thimbleware.jmemcached.storage.hash.ConcurrentLinkedHashMap;
import org.jasig.cas.server.authentication.AttributePrincipalFactory;
import org.jasig.cas.server.authentication.AuthenticationFactory;
import org.jasig.cas.server.authentication.SerializableAttributePrincipalFactoryImpl;
import org.jasig.cas.server.authentication.SerializableAuthenticationFactoryImpl;
import org.jasig.cas.services.DefaultServicesManagerImpl;
import org.jasig.cas.services.InMemoryServiceRegistryDaoImpl;
import org.jasig.services.persondir.support.StubPersonAttributeDao;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.Collections;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class MemcachedSessionStorageImplTests extends AbstractSessionStorageTests {

    private static final InetSocketAddress ADDRESS = new InetSocketAddress("localhost", 11211);

    private static final MemCacheDaemon daemon = new MemCacheDaemon();

    static {
        final ConcurrentLinkedHashMap cacheStorage = ConcurrentLinkedHashMap.create(ConcurrentLinkedHashMap.EvictionPolicy.LRU, 3000, 10000);
        daemon.setCache(new CacheImpl(cacheStorage));
        daemon.setAddr(ADDRESS);
        daemon.setIdleTime(20000);
        daemon.setVerbose(false);
        daemon.start();
    }

    @Override
    protected SessionStorage getSessionStorage() {
        try {
        return new MemcachedSessionStorageImpl(Arrays.asList(ADDRESS), Collections.<AccessFactory>emptyList(), new DefaultServicesManagerImpl(new InMemoryServiceRegistryDaoImpl()), 5000);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected AuthenticationFactory getAuthenticationFactory() {
        return new SerializableAuthenticationFactoryImpl();
    }

    @Override
    protected AttributePrincipalFactory getAttributePrincipalFactory() {
        return new SerializableAttributePrincipalFactoryImpl(new StubPersonAttributeDao());
    }
}
