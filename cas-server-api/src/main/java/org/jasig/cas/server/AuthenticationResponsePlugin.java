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
import org.jasig.cas.server.login.LoginRequest;

/**
 * A pluggable component for the {@link org.jasig.cas.server.CentralAuthenticationService} that allows you to execute a
 * set of actions after the AuthenticationResponse has been generated (regardless of success or failure).
 *
 * @author Scott Battaglia
 * @version $Revision $Date$
 * @since 3.5
 */
public interface AuthenticationResponsePlugin {
    /**
     * Perform an action after the authentication response has been generated.
     *
     * @param loginRequest the original login request.
     * @param response the corresponding authentication response.
     */
    void handle(LoginRequest loginRequest, AuthenticationResponse response);
}
