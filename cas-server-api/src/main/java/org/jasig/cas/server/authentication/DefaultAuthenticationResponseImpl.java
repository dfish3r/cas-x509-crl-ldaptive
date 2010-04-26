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

import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5.0
 */
public final class DefaultAuthenticationResponseImpl implements AuthenticationResponse {
    private final boolean succeeded;

    private final Set<Authentication> authentication;

    private final List<GeneralSecurityException> authenticationExceptions;

    private final List<Message> authenticationMessages;

    private final Map<String, Object> attributes = new HashMap<String, Object>();

    private final AttributePrincipal attributePrincipal;

    public DefaultAuthenticationResponseImpl() {
        this(new ArrayList<GeneralSecurityException>(), new ArrayList<Message>());
    }

    public DefaultAuthenticationResponseImpl(final List<GeneralSecurityException> authenticationExceptions, final List<Message> authenticationMessages) {
        this(null, null, authenticationExceptions, authenticationMessages);
    }

    public DefaultAuthenticationResponseImpl(final Set<Authentication> authentication, final AttributePrincipal attributePrincipal, final List<GeneralSecurityException> authenticationExceptions, final List<Message> authenticationMessages) {
        this.authentication = authentication;
        this.attributePrincipal = attributePrincipal;
        this.succeeded = this.authentication != null;
        this.authenticationExceptions = Collections.unmodifiableList(authenticationExceptions);
        this.authenticationMessages = Collections.unmodifiableList(authenticationMessages);
    }

    public boolean succeeded() {
        return this.succeeded;
    }

    public Set<Authentication> getAuthentications() {
        return this.authentication;
    }

    public AttributePrincipal getPrincipal() {
        return this.attributePrincipal;
    }

    public List<GeneralSecurityException> getGeneralSecurityExceptions() {
        return this.authenticationExceptions;
    }

    public List<Message> getAuthenticationMessages() {
        return this.authenticationMessages;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
