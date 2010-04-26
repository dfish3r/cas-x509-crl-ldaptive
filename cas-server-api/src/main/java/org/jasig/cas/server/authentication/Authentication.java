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
import java.util.Map;

/**
 * Represents the successful authentication of a user and the resolution into the principal that has been identified
 * by the set of credentials.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface Authentication {

    /**
     * The date at which the authentication occurred.
     *
     * @return the date at which the authentication occurred.  This CANNOT be null.
     */
    Date getAuthenticationDate();

    /**
     * Meta data about the actual authentication that occurred.  This can include all of
     * the authentication methods, etc.
     *
     * @return the Meta meta data about the authentication.  The map can be empty, but it CANNOT be null.
     */
    Map<String, List<Object>> getAuthenticationMetaData();

    /**
     * Notes whether this authentication is designed to be a long term authentication request.
     * <p>
     * Long term authentications should, but are not required, to utilize a strong authentication mechanism.
     *
     * @return true if it is, false otherwise.
     */
    boolean isLongTermAuthentication();

    /**
     * Returns the way this thing was authenticated (i.e. Safeword, LDAP, etc.).  CANNOT be NULL.
     *
     * @return the type of authentication that was used.
     */
    String getAuthenticationMethod();
}