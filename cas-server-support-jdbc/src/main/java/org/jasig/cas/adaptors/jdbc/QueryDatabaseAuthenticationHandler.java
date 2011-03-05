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

package org.jasig.cas.adaptors.jdbc;

import org.jasig.cas.server.authentication.UserNamePasswordCredential;
import org.springframework.dao.IncorrectResultSizeDataAccessException;

import javax.inject.Inject;
import javax.security.auth.login.FailedLoginException;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;

/**
 * Class that if provided a query that returns a password (parameter of query
 * must be username) will compare that password to a translated version of the
 * password provided by the user. If they match, then authentication succeeds.
 * Default password translator is plaintext translator.
 * 
 * @author Scott Battaglia
 * @author Dmitriy Kopylenko
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class QueryDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler {

    @Inject
    public QueryDatabaseAuthenticationHandler(final DataSource dataSource) {
        super(dataSource);
    }

    @NotNull
    private String sql;

    protected final void authenticateUsernamePasswordInternal(final UserNamePasswordCredential credentials) throws GeneralSecurityException {
        final String username = getPrincipalNameTransformer().transform(credentials.getUserName());
        final String password = credentials.getPassword();

        try {
            final String dbPassword = getJdbcTemplate().queryForObject(
                this.sql, String.class, username);
            if (getPasswordEncoder().isValidPassword(dbPassword, password, null)) {
                return;
            }
            throw new FailedLoginException();
        } catch (final IncorrectResultSizeDataAccessException e) {
            // this means the username was not found.
            throw new GeneralSecurityException(e);
        }
    }

    /**
     * @param sql The sql to set.
     */
    public void setSql(final String sql) {
        this.sql = sql;
    }
}