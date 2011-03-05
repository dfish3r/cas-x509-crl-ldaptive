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

package org.jasig.cas.adaptors.ldap;

import org.jasig.cas.server.authentication.GeneralSecurityExceptionTranslator;
import org.jasig.cas.server.authentication.GeneralSecurityExceptionTranslatorAuthenticationErrorCallback;
import org.jasig.cas.server.authentication.UserNamePasswordCredential;
import org.jasig.cas.server.util.LdapUtils;
import org.springframework.ldap.core.ContextSource;

import javax.inject.Inject;
import java.security.GeneralSecurityException;

/**
 * Implementation of an LDAP handler to do a "fast bind." A fast bind skips the
 * normal two step binding process to determine validity by providing before
 * hand the path to the uid.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.3
 */
public class FastBindLdapAuthenticationHandler extends AbstractLdapUsernamePasswordAuthenticationHandler {

    @Inject
    public FastBindLdapAuthenticationHandler(final ContextSource contextSource, final GeneralSecurityExceptionTranslator generalSecurityExceptionTranslator) {
        super(contextSource, generalSecurityExceptionTranslator);
    }

    protected final void authenticateUsernamePasswordInternal(final UserNamePasswordCredential credentials) throws GeneralSecurityException {

        final String transformedUsername = getPrincipalNameTransformer().transform(credentials.getUserName());
        final String bindDn = LdapUtils.getFilterWithValues(getFilter(), transformedUsername);
        final GeneralSecurityExceptionTranslatorAuthenticationErrorCallback callback = new GeneralSecurityExceptionTranslatorAuthenticationErrorCallback(getGeneralSecurityExceptionTranslator());
        final boolean authenticated = getLdapTemplate().authenticate("",  bindDn, credentials.getPassword(), callback);

        if (callback.hasGeneralSecurityException()) {
            throw callback.getGeneralSecurityException();
        }

        if (!authenticated) {
            throw new GeneralSecurityException();
        }
    }
}