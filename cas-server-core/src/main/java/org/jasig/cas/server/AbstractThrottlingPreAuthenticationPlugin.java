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

import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.authentication.DefaultAuthenticationResponseImpl;
import org.jasig.cas.server.login.DefaultLoginResponseImpl;
import org.jasig.cas.server.login.LoginRequest;
import org.jasig.cas.server.login.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.security.auth.login.AccountLockedException;
import javax.validation.constraints.Min;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract functionality for handling throttling the user.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractThrottlingPreAuthenticationPlugin implements PreAuthenticationPlugin, AuthenticationResponsePlugin {

    private static final int DEFAULT_FAILURE_THRESHOLD = 100;

    private static final int DEFAULT_FAILURE_RANGE_IN_SECONDS = 60;

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Min(0)
    private int failureThreshold = DEFAULT_FAILURE_THRESHOLD;

    @Min(0)
    private int failureRangeInSeconds = DEFAULT_FAILURE_RANGE_IN_SECONDS;

    protected final int getFailureThreshold() {
        return this.failureThreshold;
    }

    protected final int getFailureRangeAnSeconds() {
        return this.failureRangeInSeconds;
    }

    public final void setFailureThreshold(final int failureThreshold) {
        this.failureThreshold = failureThreshold;
    }

    public final void setFailureRangeInSeconds(final int failureRangeInSeconds) {
        this.failureRangeInSeconds = failureRangeInSeconds;
    }

    public final LoginResponse continueWithAuthentication(final LoginRequest loginRequest) {
        final int count = findCount(loginRequest);

        if (count >= this.failureThreshold) {
            updateCount(loginRequest);
            log.warn("*** Possible Hacking Attempt from [" + loginRequest.getRemoteIpAddress() + "].  More than " + this.failureThreshold + " failed login attempts within " + this.failureRangeInSeconds + " seconds.");
            // TODO add message text here
            final Map<Credential, List<GeneralSecurityException>> exceptions = new HashMap<Credential, List<GeneralSecurityException>>();
            exceptions.put(loginRequest.getCredentials().get(0), Arrays.asList((GeneralSecurityException) new AccountLockedException()));
            return new DefaultLoginResponseImpl(new DefaultAuthenticationResponseImpl(exceptions));
        }

        return null;
    }

    public final void handle(final LoginRequest loginRequest, final AuthenticationResponse response) {
        if (response.succeeded()) {
            return;
        }

        updateCount(loginRequest);
    }

    protected abstract int findCount(LoginRequest loginRequest);

    protected abstract void updateCount(LoginRequest loginRequest);

}
