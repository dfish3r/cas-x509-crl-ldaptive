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
import org.jasig.cas.server.session.Access;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Represents a request for a user to create a session with the system.  The basic LoginRequest interface is designed
 * to handle the most common cases.  Developers who need more specific features (such as forceAuthentication when
 * the IP address changes) may extend the interface.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface LoginRequest extends Serializable {

    /**
     * Returns the set of credentials that have been collected from the user.
     *
     * @return the set of credentials provided by the user.  This CANNOT be null, but can be empty.
     */
    List<Credential> getCredentials();

    /**
     * Returns the date the login request was created.
     *
     * @return the date the login request was created.  CANNOT be null.  MUST be immutable.
     */
    Date getDate();

    /**
     * Determines whether the user must authenticate, even if they have already authenticated (i.e. provided a
     * valid session id).  Extensions to this interface can provide more fine-grained force authentication than the
     * original protocol supports.  However, they are OPTIONAL and every CAS server should work with the basic
     * forceAuthentication.
     *
     * @return true, if authentication is being forced, false otherwise.
     */
    boolean isForceAuthentication();

    /**
     * Returns whether this is an expected ServiceAccessRequest.  Saves from having to do casting.
     *
     * @return true if it is, false otherwise.
     */
    boolean isAccessRequest();

    /**
     * Returns the remote IP address of the user.
     *
     * @return the remote IP address of the user.  CANNOT be null.
     */
    String getRemoteIpAddress();

    /**
     * Returns the current session identifier if there is one.
     *
     * @return the current session identifier, if there is one.
     */
    String getSessionId();

    /**
     * Allows you to provide a session identifier.
     *
     * @param sessionId the session id if provided later.
     */
    void setSessionId(String sessionId);

    /**
     * Determines whether the request is for a long term session or not.
     *
     * @return true if it is, false otherwise.
     */
    boolean isLongTermLoginRequest();

    /**
     * If there was an access that triggered this login request.  CAN be null.
     *
     * @return the original access or null.
     */
    Access getOriginalAccess();
}
