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

import org.jasig.cas.adaptors.jdbc.QueryDatabaseAuthenticationHandler;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class QueryDatabaseAuthenticationHandlerTests extends AbstractJdbcTests {

    @Test
    public void validUsernameAndPassword() throws Exception {
        final QueryDatabaseAuthenticationHandler a = new QueryDatabaseAuthenticationHandler(getDataSource());
        a.setSql("select password from users where user_name = ?");
        final DefaultUserNamePasswordCredential c = new DefaultUserNamePasswordCredential("foo", "bar");
        assertTrue(a.authenticate(c));
    }

    @Test
    public void validUsernameInvalidPassword() throws Exception {
        final QueryDatabaseAuthenticationHandler a = new QueryDatabaseAuthenticationHandler(getDataSource());
        a.setSql("select password from users where user_name = ?");
        final DefaultUserNamePasswordCredential c = new DefaultUserNamePasswordCredential("foo", "ha");
        assertFalse(a.authenticate(c));
    }

    @Test
    public void noUser() throws Exception {
        final QueryDatabaseAuthenticationHandler a = new QueryDatabaseAuthenticationHandler(getDataSource());
        a.setSql("select password from users where user_name = ?");
        final DefaultUserNamePasswordCredential c = new DefaultUserNamePasswordCredential("nope", "ha");
        assertFalse(a.authenticate(c));
    }


}
