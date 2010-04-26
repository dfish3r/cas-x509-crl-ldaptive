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
 * Compares a supplied (providedPassword) password against an encoded password that may be stored in a database, etc.
 * using the (optional) supplied salt.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface PasswordEncoder {

    /**
     * Determines whether a password is valid or not.
     *
     * @param encodedPassword the encoded password from the authentication system.
     * @param providedPassword the password provided by the user.
     * @param salt the optional salt to use with the algorithm.
     * @return true if the password is valid, false otherwise.
     */
    boolean isValidPassword(String encodedPassword, String providedPassword, Object salt);
}
