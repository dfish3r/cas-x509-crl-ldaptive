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
package org.jasig.cas.server.login;

import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.authentication.Message;
import org.jasig.cas.server.session.Session;

import java.util.Date;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import java.security.GeneralSecurityException;

/**
 * Default implementation of the {@link LoginResponse} that provides the methods for the basic interface.  Extending
 * this class means that the standard methods on the interface are provided, and CANNOT be changed, but you are free
 * to add additional methods.
 * <p>
 * This class, in its current state, is immutable.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class DefaultLoginResponseImpl implements LoginResponse {

    private final Date date = new Date();

    private final Session session;

    private final List<GeneralSecurityException> authenticationExceptions;

    private final List<Message> authenticationMessages;

    private final Map<String, Object> attributes;

    public DefaultLoginResponseImpl(final Session session, final AuthenticationResponse authenticationResponse) {
        this.session = session;
        
        if (authenticationResponse != null) {
            this.authenticationExceptions = Collections.unmodifiableList(authenticationResponse.getGeneralSecurityExceptions());
            this.authenticationMessages = Collections.unmodifiableList(authenticationResponse.getAuthenticationMessages());
            this.attributes = Collections.unmodifiableMap(authenticationResponse.getAttributes());
        } else {
            this.authenticationExceptions = Collections.emptyList();
            this.authenticationMessages = Collections.emptyList();
            this.attributes = Collections.emptyMap();
        }
    }

    public DefaultLoginResponseImpl(final AuthenticationResponse authenticationResponse) {
        this(null, authenticationResponse);
    }

    public final Date getDate() {
        return new Date(date.getTime());
    }

    public final Session getSession() {
        return this.session;
    }

    public final List<GeneralSecurityException> getGeneralSecurityExceptions() {
        return this.authenticationExceptions;
    }

    public final List<Message> getAuthenticationWarnings() {
        return this.authenticationMessages;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}