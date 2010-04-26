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

import java.util.*;

/**
 * Immutable version of the {@link org.jasig.cas.server.authentication.Authentication} object for use with BerkeleyDb.
 *
 * @version $Revision$ $Date$
 * @since 3.5.0
 */
public final class BerkeleyDbAuthenticationImpl implements Authentication {

    private final Date authenticationDate = new Date();

    private final boolean longTermAuthentication;

    private final Map<String, List<Object>> authenticationMetaData;

    private final String authenticationMethod;

    public BerkeleyDbAuthenticationImpl(final Map<String, List<Object>> authenticationMetaData, final boolean longTermAuthentication, final String authenticationMethod) {
        this.longTermAuthentication = longTermAuthentication;
        this.authenticationMethod = authenticationMethod;
        this.authenticationMetaData = Collections.unmodifiableMap(new HashMap<String, List<Object>>(authenticationMetaData));
    }

    public Date getAuthenticationDate() {
        return new Date(this.authenticationDate.getTime());
    }

    public Map<String, List<Object>> getAuthenticationMetaData() {
        return authenticationMetaData;
    }

    public boolean isLongTermAuthentication() {
        return this.longTermAuthentication;
    }

    public String getAuthenticationMethod() {
        return this.authenticationMethod;
    }
}
