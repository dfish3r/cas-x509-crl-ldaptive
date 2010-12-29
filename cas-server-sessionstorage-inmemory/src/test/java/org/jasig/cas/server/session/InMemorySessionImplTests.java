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

import org.jasig.cas.server.authentication.*;
import org.jasig.services.persondir.support.StubPersonAttributeDao;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.*;

/**
 * Tests for the {@link org.jasig.cas.server.session.InMemorySessionImpl}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class InMemorySessionImplTests extends AbstractSessionTests {

    private InMemoryAuthenticationImplFactory authenticationFactory = new InMemoryAuthenticationImplFactory();

    private InMemoryAttributePrincipalFactoryImpl attributePrincipalFactory = new InMemoryAttributePrincipalFactoryImpl(new StubPersonAttributeDao());

    @Override
    protected Session getNewSession(final Authentication authentication, final AttributePrincipal attributePrincipal, final ServicesManager servicesManager) {
        return new InMemorySessionImpl(getExpirationPolicy(), getAccessFactories(), new HashSet<Authentication>(Arrays.asList(authentication)), attributePrincipal, servicesManager);
    }

    @Override
    protected Authentication getNewAuthentication() {
        final AuthenticationRequest authenticationRequest = mock(AuthenticationRequest.class);
        when(authenticationRequest.isLongTermAuthenticationRequest()).thenReturn(false);
        return this.authenticationFactory.getAuthentication(Collections.<String, List<Object>>emptyMap(), authenticationRequest, getAuthenticationMethod());
    }

    @Override
    protected AttributePrincipal getNewAttributePrincipal() {
        return this.attributePrincipalFactory.getAttributePrincipal("foo");
    }
}
