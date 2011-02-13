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
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.sql.DataSource;
import java.util.UUID;

/**
 * Support for the JDBC classes.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class AbstractJdbcTests {

    private DataSource dataSource;

    private JdbcTemplate jdbcTemplate;

    @Before
    public void setUp() throws Exception {
        this.dataSource = new SingleConnectionDataSource("jdbc:hsqldb:mem:" + UUID.randomUUID().toString(), "sa", "", true);
        this.jdbcTemplate = new JdbcTemplate(this.dataSource);

        this.jdbcTemplate.execute("create table users (user_name VARCHAR(256), password VARCHAR(256))");
        this.jdbcTemplate.execute("insert into users (user_name, password) values('foo', 'bar')");
    }

    protected final DataSource getDataSource() {
        return this.dataSource;
    }

    protected final JdbcTemplate getJdbcTemplate() {
        return this.jdbcTemplate;
    }
}