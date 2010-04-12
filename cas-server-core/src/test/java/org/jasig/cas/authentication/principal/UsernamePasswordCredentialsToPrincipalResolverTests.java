/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.authentication.principal;

import org.jasig.cas.TestUtils;

import junit.framework.TestCase;
import org.jasig.cas.server.authentication.AttributePrincipal;
import org.jasig.cas.server.authentication.AttributePrincipalFactory;
import org.jasig.cas.server.authentication.CredentialToPrincipalResolver;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class UsernamePasswordCredentialsToPrincipalResolverTests extends
    TestCase {

    private CredentialToPrincipalResolver resolver = new UsernamePasswordCredentialsToPrincipalResolver(new AttributePrincipalFactory() {
        public AttributePrincipal getAttributePrincipal(final String name) {
            return new AttributePrincipal() {
                public List<Object> getAttributeValues(String attribute) {
                    return null;
                }

                public Object getAttributeValue(String attribute) {
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

    public void testValidSupportsCredentials() {
        assertTrue(this.resolver.supports(TestUtils
            .getCredentialsWithSameUsernameAndPassword()));
    }

    public void testNullSupportsCredentials() {
        assertFalse(this.resolver.supports(null));
    }

    public void testInvalidSupportsCredentials() {
        assertFalse(this.resolver.supports(TestUtils
            .getHttpBasedServiceCredentials()));
    }

    public void testValidCredentials() {
        AttributePrincipal p = this.resolver.resolve(TestUtils
            .getCredentialsWithSameUsernameAndPassword());

        assertEquals(p.getName(), TestUtils
            .getCredentialsWithSameUsernameAndPassword().getUserName());
    }
}