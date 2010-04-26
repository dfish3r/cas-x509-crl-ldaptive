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

import java.util.Date;
import java.util.List;

/**
 * Represents a request to authenticate a user based on the provided credentials.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 */
public interface AuthenticationRequest {

    /**
     * The date/time the authentication request was created.  CANNOT be null.  MUST be immutable.
     *
     * @return the date/time the request was created.
     */
    Date getAuthenticationRequestDate();

    /**
     * The list of provided credentials.  CANNOT be NULL.  Should NOT be empty.
     *
     * @return the list of credentials.
     */
    List<Credential> getCredentials();

    /**
     * Whether the user requested a long term authentication request or not.  This is commonly known as "Remember Me."
     *
     * @return true if it did, false otherwise.
     */
    boolean isLongTermAuthenticationRequest();

}
