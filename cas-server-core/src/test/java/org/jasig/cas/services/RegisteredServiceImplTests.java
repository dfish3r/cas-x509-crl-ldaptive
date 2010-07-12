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

package org.jasig.cas.services;

import static org.junit.Assert.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1
 *
 */
public class RegisteredServiceImplTests {

    private RegisteredServiceImpl r = new RegisteredServiceImpl();

    @Test(expected=IllegalArgumentException.class)
    public void settersAndGetters() {
        final long ID = 1000;
        final String DESCRIPTION = "test";
        final String SERVICEID = "serviceId";
        final String THEME = "theme";
        final String NAME = "name";
        final boolean ENABLED = false;
        final boolean ALLOWED_TO_PROXY = false;
        final boolean ANONYMOUS_ACCESS = true;
        final boolean SSO_ENABLED = false;
        final List<String> ALLOWED_ATTRIBUTES = Arrays.asList("Test");
        
        this.r.setAllowedAttributes(ALLOWED_ATTRIBUTES);
        this.r.setAllowedToProxy(ALLOWED_TO_PROXY);
        this.r.setAnonymousAccess(ANONYMOUS_ACCESS);
        this.r.setDescription(DESCRIPTION);
        this.r.setEnabled(ENABLED);
        this.r.setId(ID);
        this.r.setName(NAME);
        this.r.setServiceId(SERVICEID);
        this.r.setSsoEnabled(SSO_ENABLED);
        this.r.setTheme(THEME);
        
        assertEquals(ALLOWED_ATTRIBUTES, this.r.getAllowedAttributes());
        assertEquals(ALLOWED_TO_PROXY, this.r.isAllowedToProxy());
        assertEquals(ANONYMOUS_ACCESS, this.r.isAnonymousAccess());
        assertEquals(DESCRIPTION, this.r.getDescription());
        assertEquals(ENABLED, this.r.isEnabled());
        assertEquals(ID, this.r.getId());
        assertEquals(NAME, this.r.getName());
        assertEquals(SERVICEID, this.r.getServiceId());
        assertEquals(SSO_ENABLED, this.r.isSsoEnabled());
        assertEquals(THEME, this.r.getTheme());
        
        assertFalse(this.r.equals(null));
        assertFalse(this.r.equals(new Object()));
        assertTrue(this.r.equals(this.r));
        
        this.r.setAllowedAttributes(null);
    }

    @Test
    public void equals() {
        assertTrue(new RegisteredServiceImpl().equals(new RegisteredServiceImpl()));
        assertFalse(new RegisteredServiceImpl().equals(null));
        assertFalse(new RegisteredServiceImpl().equals(new Object()));
    }
}
