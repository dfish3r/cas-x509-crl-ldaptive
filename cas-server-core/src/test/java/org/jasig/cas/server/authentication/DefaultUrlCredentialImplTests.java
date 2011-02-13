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

import java.net.URL;

import org.jasig.cas.TestUtils;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class DefaultUrlCredentialImplTests {

    @Test
    public void urlsMatch() throws Exception {
        final URL url = new URL("http://www.cnn.com");
        final DefaultUrlCredentialImpl c = new DefaultUrlCredentialImpl(url);
        assertEquals(url, c.getUrl());
    }

    @Test
    public void properUrl() {
        assertEquals(TestUtils.CONST_GOOD_URL, TestUtils.getHttpBasedServiceCredentials().getUrl().toExternalForm());
    }

    @Test
    public void equalsWithNull() throws Exception {
        final UrlCredential c = new DefaultUrlCredentialImpl("http://www.cnn.com");
        
        assertFalse(c.equals(null));
    }

    @Test
    public void equalsWithFalse() throws Exception {
        final DefaultUrlCredentialImpl c = new DefaultUrlCredentialImpl(new URL("http://www.cnn.com"));
        final DefaultUrlCredentialImpl c2 = new DefaultUrlCredentialImpl(new URL("http://www.msn.com"));
        
        assertFalse(c.equals(c2));
        assertFalse(c.equals(new Object()));
    }

    @Test
    public void equalsWithTrue() throws Exception {
        final DefaultUrlCredentialImpl c = new DefaultUrlCredentialImpl(new URL("http://www.cnn.com"));
        final DefaultUrlCredentialImpl c2 = new DefaultUrlCredentialImpl(new URL("http://www.cnn.com"));
        
        assertTrue(c.equals(c2));
        assertTrue(c2.equals(c));
    }
}