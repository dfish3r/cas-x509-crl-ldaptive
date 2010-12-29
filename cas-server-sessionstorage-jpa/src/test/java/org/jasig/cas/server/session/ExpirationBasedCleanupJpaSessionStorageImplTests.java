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

import org.jasig.cas.server.authentication.AttributePrincipalFactory;
import org.jasig.cas.server.authentication.AuthenticationFactory;
import org.jasig.cas.server.authentication.JpaAttributePrincipalFactory;
import org.jasig.cas.server.authentication.JpaAuthenticationFactory;
import org.jasig.services.persondir.support.StubPersonAttributeDao;
import org.junit.runner.RunWith;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.inject.Qualifier;

/**
 * Tests for the {@link org.jasig.cas.server.session.AbstractJpaSessionStorageImpl}.
 *
 * @author Scott Battaglia
 * @version $Revision: 21002 $ $Date: 2010-07-05 01:00:58 -0400 (Mon, 05 Jul 2010) $
 * @since 4.0.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations= {"classpath:simpleApplicationContext.xml"})
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionalTestExecutionListener.class})
@TransactionConfiguration
@Transactional()
public final class ExpirationBasedCleanupJpaSessionStorageImplTests extends AbstractSessionStorageTests implements ApplicationContextAware {

    protected ApplicationContext applicationContext;

    @Resource(name = "expirationBasedCleanupJpaSessionStorage")
    private SessionStorage sessionStorage;

    @Override
    protected SessionStorage getSessionStorage() {
        return this.sessionStorage;
    }

    @Override
    protected AuthenticationFactory getAuthenticationFactory() {
        return new JpaAuthenticationFactory();
    }

    @Override
    protected AttributePrincipalFactory getAttributePrincipalFactory() {
        return new JpaAttributePrincipalFactory(new StubPersonAttributeDao());
    }

    public void setApplicationContext(final ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
