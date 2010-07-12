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
import org.jasig.cas.server.authentication.UserNamePasswordCredential;
import org.jasig.cas.server.login.LoginRequest;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractThrottledPreAuthenticationPluginTests {

    protected static final int CONST_FAILURE_THRESHHOLD = 3;

    protected static final int CONST_FAILURE_TIMEOUT = 2;

    private LoginRequest loginRequest;

    private AbstractThrottlingPreAuthenticationPlugin plugin;

    protected abstract AbstractThrottlingPreAuthenticationPlugin getPreAuthenticationPlugin();

    protected abstract void decrementCount(AbstractThrottlingPreAuthenticationPlugin plugin);

    @Before
    public final void setUp() {
        this.loginRequest = mock(LoginRequest.class);
        when(this.loginRequest.getRemoteIpAddress()).thenReturn("111.111.111.111");

        final UserNamePasswordCredential c = mock(UserNamePasswordCredential.class);
        when(c.getUserName()).thenReturn("foo");

        when(this.loginRequest.getCredentials()).thenReturn(Arrays.asList((Credential) c));

        this.plugin = getPreAuthenticationPlugin();
    }

   @Test
    public final void oneFailure() throws Exception {
       final AuthenticationResponse response = mock(AuthenticationResponse.class);
       when(response.succeeded()).thenReturn(false);

       this.plugin.handle(this.loginRequest, response);

        assertEquals(1, this.plugin.findCount(this.loginRequest));
        assertNull(this.plugin.continueWithAuthentication(loginRequest));
    }

    @Test
    public final void success() throws Exception {
        final AuthenticationResponse response = mock(AuthenticationResponse.class);
        when(response.succeeded()).thenReturn(true);

        this.plugin.handle(this.loginRequest, response);

        assertEquals(0, this.plugin.findCount(this.loginRequest));
        assertNull(this.plugin.continueWithAuthentication(loginRequest));
    }

    @Test
    public final void enoughFailuresToCauseProblem() throws Exception {
        final AuthenticationResponse response = mock(AuthenticationResponse.class);
        when(response.succeeded()).thenReturn(false);

        for (int i = 0; i < CONST_FAILURE_THRESHHOLD+1; i++) {
            this.plugin.handle(this.loginRequest, response);
        }

        assertNotNull(this.plugin.continueWithAuthentication(loginRequest));
    }

    @Test
    public final void failuresThenSuccess() throws Exception {
        final AuthenticationResponse response = mock(AuthenticationResponse.class);
        when(response.succeeded()).thenReturn(false);

        for (int i = 0; i < CONST_FAILURE_THRESHHOLD+1; i++) {
            this.plugin.handle(this.loginRequest, response);
        }

        assertNotNull(this.plugin.continueWithAuthentication(loginRequest));

        for (int i = 0; i < CONST_FAILURE_THRESHHOLD; i++) {
            decrementCount(this.plugin);
        }

        assertNull(this.plugin.continueWithAuthentication(loginRequest));
    }
}
