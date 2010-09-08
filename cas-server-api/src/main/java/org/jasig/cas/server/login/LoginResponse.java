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

import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.authentication.Message;
import org.jasig.cas.server.session.Session;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.security.GeneralSecurityException;
import java.io.Serializable;

/**
 * Represents a response from the CentralAuthenticationService after a login request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface LoginResponse extends Serializable {

    /**
     * Returns the date the login response was created.
     *
     * @return the date the login response was created.  CANNOT be null.  MUST be immutable.
     */
    Date getDate();

    /**
     * If a session was created it should be returned in the response.  Depending on the implementation of
     * the service, the session identifier can change on each request and systems that refer to the identifier should always
     * update their identifier to use the most recent one returned.
     * <p>
     * This CAN be null.
     *
     * @return the session created with the login request.
     */
    Session getSession();

    /**
     * Returns the map of problems, if there were any, with this login request.
     *
     * This list can be empty but NOT null.
     * @return the map of authentication exceptions.
     */
    Map<Credential, List<GeneralSecurityException>> getGeneralSecurityExceptions();

    /**
     * A list of warnings, as Message objects, that were accompanied with this request.
     * These should be displayed to the user.
     *
     * The list can be empty but NOT null.
     * @return the list of authentication warnings (i.e. your password will expire in 3 days)
     */
    List<Message> getAuthenticationWarnings();

    /**
     * Returns the set of attributes associated with this login response.  CANNOT be null.  CAN be empty.  Immutable.
     * @return the map of attributes.
     */
    Map<String, Object> getAttributes();
}
