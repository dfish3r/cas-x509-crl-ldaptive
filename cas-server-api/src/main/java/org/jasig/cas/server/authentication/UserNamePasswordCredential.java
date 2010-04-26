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
 * Represents the traditional user name/password combination that the majority of users would provide as
 * a form of credentials.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface UserNamePasswordCredential extends Credential {

    /**
     * Retrieves the user name that was provided.
     *
     * @return the user name.  This should NEVER be null.
     */
    String getUserName();

    /**
     * Retrieves the password that was provided.
     *
     * @return the password.  This should NEVER be null.
     */
    String getPassword();
}
