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

import org.jasig.cas.server.session.Access;

import java.util.List;

/**
 * Represents the response to a request for access to a resource.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface ServiceAccessResponse extends LoginResponse {

    /**
     * The access requested.  CANNOT be null.
     * @return the access requested.
     */
    Access getAccess();

    /**
     * Returns the Access requests that might have been invalidated when a re-authentication was required.
     *
     * @return the list of accesses.  CANNOT be NULL.  CAN be EMPTY.
     */
    List<Access> getLoggedOutAccesses();
}