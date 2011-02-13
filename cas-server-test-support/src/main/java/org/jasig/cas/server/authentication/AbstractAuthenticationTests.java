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

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractAuthenticationTests {

    private final String CONST_AUTHENTICATION_METHOD = "foo";

    private final Map<String, List<Object>> CONST_AUTHENTICATION_META_DATA = new HashMap<String, List<Object>>();

    private Authentication authentication;

    protected abstract Authentication getAuthentication(Map<String, List<Object>> metaData, String authenticationMethod);

    @Before
    public final void setUp() throws Exception {
        this.authentication = getAuthentication(CONST_AUTHENTICATION_META_DATA, CONST_AUTHENTICATION_METHOD);
    }

    @Test
    public final void getters() {
        assertEquals(CONST_AUTHENTICATION_METHOD, this.authentication.getAuthenticationMethod());
        assertEquals(CONST_AUTHENTICATION_META_DATA, this.authentication.getAuthenticationMetaData());
        assertFalse(this.authentication.isLongTermAuthentication());
    }

    public final void equalsAndHashcodeMethod() {
    final Authentication other = getAuthentication(CONST_AUTHENTICATION_META_DATA, CONST_AUTHENTICATION_METHOD);
        assertTrue(this.authentication.equals(other));
        assertEquals(this.authentication.hashCode(), other.hashCode());
    }
}
