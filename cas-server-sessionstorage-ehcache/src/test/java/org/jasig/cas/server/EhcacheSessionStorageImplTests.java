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
import org.jasig.cas.server.session.EhcacheSessionStorageImpl;
import org.jasig.cas.server.session.SessionStorage;
import org.jasig.services.persondir.support.StubPersonAttributeDao;

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
        return new SerializableAttributePrincipalFactoryImpl(new StubPersonAttributeDao());
    }
}
