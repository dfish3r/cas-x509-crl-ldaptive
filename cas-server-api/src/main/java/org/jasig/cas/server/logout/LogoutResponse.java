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

package org.jasig.cas.server.logout;

import org.jasig.cas.server.session.Access;

import java.io.Serializable;
import java.util.List;
import java.util.Date;

/**
 * The response to a request to invalidate an existing single sign on session.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface LogoutResponse extends Serializable {

    /**
     * Returns the date when the logout response was generated.
     * <p>
     * This CANNOT be null.  MUST be immutable.
     *
     * @return the date the logout response took place.
     */
    Date getDate();

    /**
     * Retrieve the list of services that we were already successfully able to log out of.
     * <p>
     * This CANNOT be null.  CAN be empty.
     *
     * @return the list of services we already logged out of.
     */
    List<Access> getLoggedOutAccesses();

    /**
     * The list of services that we have not logged out of yet.
     * <p>
     * CANNOT be null.  CAN be empty.
     *
     * @return the list of services that we have not logged out of yet.
     */
    List<Access> getLoggedInAccesses();
}