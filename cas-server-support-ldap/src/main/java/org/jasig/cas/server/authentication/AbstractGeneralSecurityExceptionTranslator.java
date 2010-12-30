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
import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Base class for translating {@link AuthenticationException} to {@link GeneralSecurityException}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public abstract class AbstractGeneralSecurityExceptionTranslator implements GeneralSecurityExceptionTranslator {

    @NotNull
    private final Map<Pattern, Class<? extends GeneralSecurityException>> mappings = new HashMap<Pattern, Class<? extends GeneralSecurityException>>();

    protected AbstractGeneralSecurityExceptionTranslator(final Map<String, Class<? extends GeneralSecurityException>> userMappings) {
        for (final Map.Entry<String, Class<? extends GeneralSecurityException>> entry : userMappings.entrySet()) {
            this.mappings.put(Pattern.compile(entry.getKey()), entry.getValue());
        }
    }

    protected final void registerErrorPattern(final Pattern pattern, final Class<? extends GeneralSecurityException> eClass) {
        if (!this.mappings.containsKey(pattern)) {
            this.mappings.put(pattern, eClass);
        }
    }

    protected final void registerErrorPattern(final String pattern, final Class<? extends GeneralSecurityException> eClass) {
        registerErrorPattern(Pattern.compile(pattern), eClass);
    }

    private GeneralSecurityException translateExceptionIfPossible(final String explanation, final String message) {

        for (final Map.Entry<Pattern, Class<? extends GeneralSecurityException>> entry : this.mappings.entrySet()) {
            if (entry.getKey().matcher(explanation).matches()) {
                try {
                    return entry.getValue().getConstructor(String.class).newInstance(message);
                } catch (final Exception ex) {
                    // this really shouldn't happen;
                }
            }
        }

        return new UncategorizedSecurityException(message);
    }

    public final GeneralSecurityException translateExceptionIfPossible(final AuthenticationException e) {
        return translateExceptionIfPossible(e.getExplanation(), e.getMessage());
    }

    public final GeneralSecurityException translateExceptionIfPossible(final NamingException e) {
        return translateExceptionIfPossible(e.getExplanation(), e.getMessage());
    }
}
