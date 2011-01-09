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

import org.springframework.ldap.AuthenticationException;

import javax.naming.NamingException;
import java.security.GeneralSecurityException;

/**
 * Implementation that doesn't do anything.  Useful only when we don't have an actual mapping.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class NullGeneralSecurityExceptionTranslator implements GeneralSecurityExceptionTranslator {

    public GeneralSecurityException translateExceptionIfPossible(final AuthenticationException e) {
        return null;
    }

    public GeneralSecurityException translateExceptionIfPossible(final NamingException e) {
        return null;
    }
}
