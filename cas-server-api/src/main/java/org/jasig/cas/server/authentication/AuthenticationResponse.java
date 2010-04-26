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

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Represents a response to an authentication request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 */
public interface AuthenticationResponse {

    /**
     * Notes whether the authentication attempt succeeded or not.
     *
     * @return true if it did, false otherwise.
     */
    boolean succeeded();

    /**
     * Returns information about the successful authentication.  This could include the principal, time, etc.
     *
     * If succeeded == true, then this CANNOT be null.
     *
     * @return the authentication, or null if authentication failed.
     */
    Set<Authentication> getAuthentications();

    /**
     * Returns the principal associated with this set of authentications.
     *
     * @return the principal.  CANNOT be NULL if succeeded=true
     */
    AttributePrincipal getPrincipal();

    /**
     * Contains the list of GeneralSecurityExceptions that may have occurred during the course of the Authentication.
     * <p>
     * This makes no assumptions about whether authentication succeeded or failed based on these exceptions, so one should
     * ALWAYS check succeeded.
     * <p>
     * This list can be empty but CANNOT be null.
     * @return the list of authentication exceptions.  CANNOT be null.
     */
    List<GeneralSecurityException> getGeneralSecurityExceptions();

    /**
     * Retrieve the list of Authentication messages, which may be things like "Your password expires in 2 days.".
     * This makes no assumptions about whether authentication succeeded or failed based on these exceptions, so one should
     * ALWAYS check succeeded.
     * <p>
     * This list can be empty but CANNOT be null.
     *
     * @return the list of authentication messages. CANNOT be null.
     */
    List<Message> getAuthenticationMessages();

    /**
     * A list of attributes related to the authentication response.  Examples can be CAPTCHA ids. This map is NOT immutable.
     * @return the map.  CAN be empty.  CANNOT be null.
     */
    Map<String, Object> getAttributes();
}
