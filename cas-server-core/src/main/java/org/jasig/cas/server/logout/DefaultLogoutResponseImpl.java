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

package org.jasig.cas.server.logout;

import org.jasig.cas.server.session.Access;
import org.jasig.cas.server.session.Session;

import java.util.*;

/**
 * Default implementation of the {@link LogoutResponse}
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class DefaultLogoutResponseImpl implements LogoutResponse {

    private final Date date = new Date();

    private final List<Access> loggedOutAccesses;

    private final List<Access> loggedInAccesses;

    /**
     * Default constructor that reads the accesses from the session and sorts them into the appropriate lists.
     * <p>
     * Does NOT retain a link to the session.
     *
     * @param session the session to grab the accesses from.
     */
    public DefaultLogoutResponseImpl(final Session session) {
        this(Arrays.asList(session));
    }

    public DefaultLogoutResponseImpl(final Collection<Session> sessions) {
        final List<Access> loggedOutAccesses = new ArrayList<Access>();
        final List<Access> loggedInAccesses = new ArrayList<Access>();

        for (final Session session : sessions) {
            for (final Access access : session.getAccesses()) {
                if (access.isLocalSessionDestroyed()) {
                    loggedOutAccesses.add(access);
                } else {
                    loggedInAccesses.add(access);
                }
            }
        }

        this.loggedInAccesses = Collections.unmodifiableList(loggedInAccesses);
        this.loggedOutAccesses = Collections.unmodifiableList(loggedOutAccesses);
    }

    public DefaultLogoutResponseImpl() {
        this.loggedInAccesses = Collections.emptyList();
        this.loggedOutAccesses = Collections.emptyList();
    }

    public Date getDate() {
        return new Date(this.date.getTime());
    }

    public List<Access> getLoggedOutAccesses() {
        return this.loggedOutAccesses;
    }

    public List<Access> getLoggedInAccesses() {
        return this.loggedInAccesses;
    }
}