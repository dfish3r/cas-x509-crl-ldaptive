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

package org.jasig.cas.server.authentication;

import org.jasig.cas.TestUtils;

import org.jasig.cas.server.authentication.AttributePrincipal;
import org.jasig.cas.server.authentication.AttributePrincipalFactory;
import org.jasig.cas.server.authentication.CredentialToPrincipalResolver;
import org.jasig.cas.server.authentication.UrlCredentialToPrincipalResolver;
import org.junit.Test;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class UrlCredentialToPrincipalResolverTests {

    private CredentialToPrincipalResolver resolver = new UrlCredentialToPrincipalResolver(new AttributePrincipalFactory() {
        public AttributePrincipal getAttributePrincipal(final String name) {
            return new AttributePrincipal() {
                public List<Object> getAttributeValues(final String attribute) {
                    return null;
                }

                public Object getAttributeValue(final String attribute) {
                    return null;
                }

                public Map<String, List<Object>> getAttributes() {
                    return Collections.emptyMap();
                }

                public String getName() {
                    return name;
                }
            };
        }
    });

    @Test
    public void inValidSupportsCredentials() {
        assertFalse(this.resolver.supports(TestUtils.getCredentialsWithSameUsernameAndPassword()));
    }

    @Test
    public void nullSupportsCredentials() {
        assertFalse(this.resolver.supports(null));
    }

    @Test
    public void validSupportsCredentials() {
        assertTrue(this.resolver.supports(TestUtils.getHttpBasedServiceCredentials()));
    }

    @Test
    public void validCredentials() {
        assertEquals(this.resolver.resolve(TestUtils.getHttpBasedServiceCredentials()).getName(), TestUtils.getHttpBasedServiceCredentials().getUrl().toExternalForm());
    }
}