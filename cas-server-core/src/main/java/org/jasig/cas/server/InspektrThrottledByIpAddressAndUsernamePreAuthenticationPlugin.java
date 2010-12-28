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

package org.jasig.cas.server;

import com.github.inspektr.audit.AuditActionContext;
import com.github.inspektr.audit.AuditTrailManager;
import com.github.inspektr.common.web.ClientInfo;
import com.github.inspektr.common.web.ClientInfoHolder;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.authentication.UserNamePasswordCredential;
import org.jasig.cas.server.login.LoginRequest;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.inject.Inject;
import javax.sql.DataSource;
import javax.validation.constraints.NotNull;
import java.sql.Types;
import java.util.Calendar;
import java.util.Date;

/**
 * Works in conjunction with the Inspektr Library to block attempts to dictionary attack users.
 * <p>
 * Defines a new Inspektr Action "THROTTLED_LOGIN_ATTEMPT" which keeps track of failed login attempts that don't result
 * in AUTHENTICATION_FAILED methods
 * <p>
 * This relies on the default Inspektr table layout and username construction.  The username construction can be overrided
 * in a subclass.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class InspektrThrottledByIpAddressAndUsernamePreAuthenticationPlugin extends AbstractThrottlingPreAuthenticationPlugin {

    private static final String DEFAULT_APPLICATION_CODE = "CAS";

    private static final String INSPEKTR_ACTION = "THROTTLED_LOGIN_ATTEMPT";

    @NotNull
    private final AuditTrailManager auditTrailManager;

    @NotNull
    private final JdbcTemplate jdbcTemplate;

    @NotNull
    private String applicationCode = DEFAULT_APPLICATION_CODE;

    @Inject
    public InspektrThrottledByIpAddressAndUsernamePreAuthenticationPlugin(final AuditTrailManager auditTrailManager, final DataSource dataSource) {
        this.auditTrailManager = auditTrailManager;
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    protected int findCount(final LoginRequest loginRequest) {
        final String SQL = "Select count(*) from COM_AUDIT_TRAIL where AUD_CLIENT_IP = ? and AUD_USER = ? AND AUD_ACTION = ? AND APPLIC_CD = ? AND AUD_DATE >= ?";
        final String userToUse = constructUsername(loginRequest);
        final Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.SECOND, -1 * getFailureRangeAnSeconds());
        final Date oldestDate = calendar.getTime();
        return this.jdbcTemplate.queryForInt(SQL, new Object[] {loginRequest.getRemoteIpAddress(), userToUse, INSPEKTR_ACTION, this.applicationCode, oldestDate}, new int[] {Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.VARCHAR, Types.TIMESTAMP});
    }

    @Override
    protected void updateCount(final LoginRequest loginRequest) {
        final String userToUse = constructUsername(loginRequest);
        final ClientInfo clientInfo = ClientInfoHolder.getClientInfo();
        final AuditActionContext context = new AuditActionContext(userToUse, userToUse, INSPEKTR_ACTION, this.applicationCode, new Date(), clientInfo.getClientIpAddress(), clientInfo.getServerIpAddress());
        this.auditTrailManager.record(context);
    }

    public final void setApplicationCode(final String applicationCode) {
        this.applicationCode = applicationCode;
    }

    protected String constructUsername(final LoginRequest loginRequest) {
        for (final Credential c : loginRequest.getCredentials()) {
            if (c instanceof UserNamePasswordCredential) {
                final UserNamePasswordCredential upc = (UserNamePasswordCredential) c;
                return "[username: " + upc.getUserName().toLowerCase() + "]";
            }
        }

        return "[username: ]";
    }
}
