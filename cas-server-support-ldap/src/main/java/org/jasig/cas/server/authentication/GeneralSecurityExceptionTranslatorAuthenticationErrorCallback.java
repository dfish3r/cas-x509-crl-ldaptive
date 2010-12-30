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
import org.springframework.ldap.core.AuthenticationErrorCallback;

import javax.naming.NamingException;
import java.security.GeneralSecurityException;

/**
 * Implementation of the {@link AuthenticationErrorCallback} interface to map Spring LDAP authentication attempts to {@link GeneralSecurityException}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class GeneralSecurityExceptionTranslatorAuthenticationErrorCallback implements AuthenticationErrorCallback {

    private final GeneralSecurityExceptionTranslator generalSecurityExceptionTranslator;

    private GeneralSecurityException generalSecurityException;

    /**
     * Exposes the {@link GeneralSecurityException} if there is one.
     *
     * @return the GeneralSecurityException or null if there is none.
     */
    public GeneralSecurityException getGeneralSecurityException() {
        return this.generalSecurityException;
    }

    /**
     * Returns true if we've caught a GeneralSecurityException.
     *
     * @return true if there is one, false otherwise.
     */
    public boolean hasGeneralSecurityException() {
        return this.generalSecurityException != null;
    }

    public GeneralSecurityExceptionTranslatorAuthenticationErrorCallback(final GeneralSecurityExceptionTranslator generalSecurityExceptionTranslator) {
        this.generalSecurityExceptionTranslator = generalSecurityExceptionTranslator;
    }

    public void execute(final Exception e) {
        if (e instanceof NamingException) {
            this.generalSecurityException = this.generalSecurityExceptionTranslator.translateExceptionIfPossible((NamingException) e);
        }

        if (e instanceof AuthenticationException) {
            this.generalSecurityException = this.generalSecurityExceptionTranslator.translateExceptionIfPossible((AuthenticationException) e);
        }
    }
}
