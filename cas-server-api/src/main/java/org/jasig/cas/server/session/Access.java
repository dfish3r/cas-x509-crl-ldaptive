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

package org.jasig.cas.server.session;

import org.jasig.cas.server.login.TokenServiceAccessRequest;

import java.io.Serializable;

/**
 * Represents a request to access a resource.  Implementations of this interface would be specific versions of how to
 * access that resource, i.e. either via the CAS1 or CAS2 protocol, etc.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface Access extends Serializable {

    /**
     * Returns the unique identifier for this Access.
     *
     * @return the unique identifier for this access.  CANNOT be null.
     */
    String getId();

    /**
     * The identifier for this resource, i.e. the URL.
     *
     * @return the identifier for the resource.  CANNOT be null.
     */
    String getResourceIdentifier();

    /**
     * Validates the token request against this specific access.  A Validation request should modify the internal
     * state of the Access such that when the system next asks for an appropriate response to give to the calling/client
     * application, it can.
     * <p>
     * Examples:
     * <ul>
     * <li> A self-validating token may not modify the state of the system at all.</li>
     * <li> A CAS implementation may modify some internal state, i.e. number of uses, etc.</li>
     * </ul>
     * @param tokenServiceAccessRequest the request for validation.
     */
    void validate(TokenServiceAccessRequest tokenServiceAccessRequest);

    /**
     * Notifies the existing system that the user's local session should be destroyed.
     *
     * @return true if the session was able to be destroyed, false otherwise.
     */
    boolean invalidate();

    /**
     * Determines if the local session was destroyed or not.
     *
     * @return true if the local session was destroyed, false otherwise.
     */
    boolean isLocalSessionDestroyed();

    /**
     * Denotes whether the Access needs to be remembered or not.  If it does not support logout and does not need
     * validation then it may not require storage.
     *
     * @return true if it requires storage, false otherwise.
     */
    boolean requiresStorage();

    /**
     * Generates the response to send back to the client.
     *
     * @param accessResponseRequest the request for a response.
     * @return the result of the request.  CANNOT be null.
     */
    AccessResponseResult generateResponse(AccessResponseRequest accessResponseRequest);
}
