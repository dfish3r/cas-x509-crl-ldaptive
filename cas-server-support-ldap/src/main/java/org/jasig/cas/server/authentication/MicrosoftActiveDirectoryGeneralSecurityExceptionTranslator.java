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

import javax.security.auth.login.AccountExpiredException;
import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.AccountNotFoundException;
import javax.security.auth.login.CredentialExpiredException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Contains the Microsoft patterns for matching to the general exceptions.
 * <p>
 * This class is thread-safe.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class MicrosoftActiveDirectoryGeneralSecurityExceptionTranslator extends AbstractGeneralSecurityExceptionTranslator {

    public MicrosoftActiveDirectoryGeneralSecurityExceptionTranslator() {
        this(Collections.<String, Class<? extends GeneralSecurityException>>emptyMap());
    }

    public MicrosoftActiveDirectoryGeneralSecurityExceptionTranslator(Map<String, Class<? extends GeneralSecurityException>> userMappings) {
        super(userMappings);
        registerErrorPattern(Pattern.compile("\\D525\\D"), AccountNotFoundException.class);
        registerErrorPattern(Pattern.compile("\\D530\\D"), CredentialExpiredException.class); // this is actually login failure due to time restriction violation.
        registerErrorPattern(Pattern.compile("\\D532\\D"), CredentialExpiredException.class);
        registerErrorPattern(Pattern.compile("\\D533\\D"), AccountLockedException.class);
        registerErrorPattern(Pattern.compile("\\D701\\D"), AccountExpiredException.class);
        registerErrorPattern(Pattern.compile("\\D773\\D"), CredentialResetException.class);
        registerErrorPattern(Pattern.compile("\\D775\\D"), AccountLockedException.class);
    }
}
