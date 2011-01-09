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
