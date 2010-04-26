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

import java.util.List;
import java.util.Map;

/**
 * Factory for creating Authentication objects.  We have various factories because depending on which backing mechanism
 * you are using, you may construct different Authentication objects.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface AuthenticationFactory {

    /**
     * Constructs a new Authentication object based on the provided principal and authentication meta data.
     *
     * @param authenticationMetaData any information about the authentication.
     * @param authenticationRequest the original request for authentication.
     * @param authenticationType the authentication type.  CANNOT be NULL.
     * @return a newly constructed authentication object.  This should NEVER return null, as only one should ever
     * be configured.
     */
    Authentication getAuthentication(Map<String, List<Object>> authenticationMetaData, AuthenticationRequest authenticationRequest, final String authenticationType);
}
