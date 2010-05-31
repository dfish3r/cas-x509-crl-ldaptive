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

/**
 * Manages the authentication of the User's Credentials by processing the Authentication Request.  AuthenticationManagers
 * should be able to authenticate the entire AuthenticationRequest.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface AuthenticationManager {

    /**
     * This method executes the authentication check and produces an AuthenticationResponse.  This should not throw an
     * exception, but capture any underlying exceptions and consolidate them into the AuthenticationResponse.
     * <p>
     * The logic behind not throwing an exception, but consolidating them in CAS3.5 is that we can then take the messages
     * and send them back to the UI and provide a nice user experience to the user.
     *
     * @param authenticationRequest the request for authentication.  CANNOT be NULL.
     * @return the response to the request.  This should NEVER be null.
     */
    AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest);
}
