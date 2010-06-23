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

package org.jasig.cas.server.logout;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultLogoutResponseImplTests {

    @Test
    public void testEmptyResponse() {
        final LogoutResponse logoutResponse = new DefaultLogoutResponseImpl();
        assertTrue(logoutResponse.getLoggedInAccesses().isEmpty());
        assertTrue(logoutResponse.getLoggedOutAccesses().isEmpty());
        assertNotNull(logoutResponse.getDate());
    }

    // TODO implement this test at some point when I bring in mockito
    public void testNonEmptyResponse() {

    }


    @Test
    public void testImmutability() {
        final LogoutResponse logoutResponse = new DefaultLogoutResponseImpl();
        assertNotNull(logoutResponse.getDate());

         final Date date = logoutResponse.getDate();
         date.setTime(500);

         assertNotSame(date, logoutResponse.getDate());
    }


}
