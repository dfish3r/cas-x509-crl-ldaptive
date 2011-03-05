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
import org.springframework.beans.factory.InitializingBean;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;

/**
 * Class that given a table, username field and password field will query a
 * database table with the provided encryption technique to see if the user
 * exists. This class defaults to a PasswordTranslator of
 * PlainTextPasswordTranslator.
 * 
 * @author Scott Battaglia
 * @author Dmitriy Kopylenko
 * @version $Revision$ $Date$
 * @since 3.0
 */

public final class SearchModeSearchDatabaseAuthenticationHandler extends AbstractJdbcUsernamePasswordAuthenticationHandler implements InitializingBean {

    private static final String SQL_PREFIX = "Select count('x') from ";

    @NotNull
    private String fieldUser;

    @NotNull
    private String fieldPassword;

    @NotNull
    private String tableUsers;

    private String sql;

    @Inject
    public SearchModeSearchDatabaseAuthenticationHandler(final DataSource dataSource) {
        super(dataSource);
    }

    protected final void authenticateUsernamePasswordInternal(final UserNamePasswordCredential credentials) throws GeneralSecurityException {
        final String transformedUsername = getPrincipalNameTransformer().transform(credentials.getUserName());
//        final String encyptedPassword = getPasswordEncoder().encode(credentials.getPassword());

        // TODO we should be comparing the encrypted one.
        final int count = getJdbcTemplate().queryForInt(this.sql, transformedUsername, credentials.getPassword());

        if (count > 0) {
            return;
        }

        throw new GeneralSecurityException();
    }

    public void afterPropertiesSet() throws Exception {
        this.sql = SQL_PREFIX + this.tableUsers + " Where " + this.fieldUser
        + " = ? And " + this.fieldPassword + " = ?"; 
    }

    /**
     * @param fieldPassword The fieldPassword to set.
     */
    public void setFieldPassword(final String fieldPassword) {
        this.fieldPassword = fieldPassword;
    }

    /**
     * @param fieldUser The fieldUser to set.
     */
    public void setFieldUser(final String fieldUser) {
        this.fieldUser = fieldUser;
    }

    /**
     * @param tableUsers The tableUsers to set.
     */
    public void setTableUsers(final String tableUsers) {
        this.tableUsers = tableUsers;
    }
}