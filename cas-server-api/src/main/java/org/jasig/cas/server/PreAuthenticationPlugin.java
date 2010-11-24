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

import org.jasig.cas.server.login.LoginRequest;
import org.jasig.cas.server.login.LoginResponse;

/**
 * Defines actions that occur before authentication.
 * <p>
 * Replacement for the AbstractPreAndPostAuthenticationHandler.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface PreAuthenticationPlugin {

    /**
     * Determines whether we should continue with authentication or not.
     *
     * @param loginRequest the login request, CANNOT be null.
     * @return a login response if we should stop processing.  NULL if we should continue.
     */
    LoginResponse continueWithAuthentication(LoginRequest loginRequest);
}