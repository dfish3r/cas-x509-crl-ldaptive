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

import org.infinispan.Cache;
import org.infinispan.manager.DefaultCacheManager;
import org.infinispan.manager.EmbeddedCacheManager;
import org.jasig.cas.server.authentication.AttributePrincipalFactory;
import org.jasig.cas.server.authentication.AuthenticationFactory;
import org.jasig.cas.server.authentication.SerializableAttributePrincipalFactoryImpl;
import org.jasig.cas.server.authentication.SerializableAuthenticationFactoryImpl;
import org.jasig.cas.services.DefaultServicesManagerImpl;
import org.jasig.cas.services.InMemoryServiceRegistryDaoImpl;
import org.jasig.services.persondir.support.StubPersonAttributeDao;

import java.util.Collections;
import java.util.List;

/**
 * Concrete test for the {@link org.jasig.cas.server.session.InfinispanSessionStorageImpl}
 *
 * @author Scott Battaglia
 * @version $Revision: 21432 $ $Date: 2010-08-08 15:44:30 -0400 (Sun, 08 Aug 2010) $
 * @since 3.5
 */
public final class InfinispanSessionStorageImplTests extends AbstractSessionStorageTests {

    final EmbeddedCacheManager ecm = new DefaultCacheManager();

    @Override
    protected SessionStorage getSessionStorage() {
        final Cache<String,SerializableSessionImpl> cache = ecm.getCache("cache");
        final Cache<String,String> cacheMappings = ecm.getCache("cacheMappings");
        final Cache<String,List<String>> principalMappings = ecm.getCache("principalMappings");
        return new InfinispanSessionStorageImpl(cache, cacheMappings, principalMappings, getAccessFactories(), new DefaultServicesManagerImpl(new InMemoryServiceRegistryDaoImpl()));
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
