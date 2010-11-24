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

package org.jasig.cas.server.login;

/**
 * Request access to a specific service identified by the provided identifier.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public interface ServiceAccessRequest extends LoginRequest {

    /**
     * The identifier for the service.  This CANNOT be null.
     *
     * @return the identifier for the service.
     */
    String getServiceId();

    /**
     * The url to redirect to should passive authentication fail.
     *
     * @return the url to redirect to should passive authentication fail.  Can be null if protocol doesn't support.  However,
     * passiveAuthentication must also be set to false in that instance.
     */
    String getPassiveAuthenticationRedirectUrl();

    /**
     * Determines whether the user should be prompted for credentials or not.  In CAS terminology, this is "gateway"
     *
     * @return true if they should not be prompted, false otherwise.
     */
    boolean isPassiveAuthentication();
}