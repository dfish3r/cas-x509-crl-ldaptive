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
import org.jasig.cas.server.session.Access;
import sun.jvm.hotspot.utilities.Assert;

import java.util.Collections;
import java.util.List;

/**
 * Default implementation of the {@link ServiceAccessResponse} that gets sent back when a user requests access
 * to a specific service.  Things that consume this response should take note that a sessionId is returned.  This is
 * because certain session implementations may change their identifiers more frequently than once per session.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class DefaultServiceAccessResponseImpl extends DefaultLoginResponseImpl implements ServiceAccessResponse {

    private final Access access;

    private final List<Access> accesses;

    public DefaultServiceAccessResponseImpl(final Access access, final List<Access> loggedOutServices, String sessionId, AuthenticationResponse authenticationResponse) {
        super(sessionId, authenticationResponse);
        this.access = access;
        this.accesses = Collections.unmodifiableList(loggedOutServices);
    }

    public DefaultServiceAccessResponseImpl(final Access access, final List<Access> loggedOutServices, AuthenticationResponse authenticationResponse) {
        this(access, loggedOutServices, null, authenticationResponse);
    }

    public final Access getAccess() {
        return this.access;
    }

    public final List<Access> getLoggedOutAccesses() {
        return this.accesses;
    }
}
