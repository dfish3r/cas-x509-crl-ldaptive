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

package org.jasig.cas.server.session;

import java.util.List;
import java.util.Set;

/**
 * Holds the logic common to all the session storage implementations that rely on the serializable-friendly classes.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public abstract class AbstractSerializableSessionStorageImpl extends AbstractSessionStorage {

    protected AbstractSerializableSessionStorageImpl(final List<AccessFactory> accessFactories, final ServicesManager servicesManager) {
        super(accessFactories, servicesManager, new MultiUseOrTimeToLiveExpirationPolicy(21600));
        AbstractStaticSession.setAccessFactories(getAccessFactories());
        AbstractStaticSession.setServicesManager(servicesManager);
    }

    protected abstract Set<Session> findSessionsByPrincipalInternal(String principalName);

    protected abstract Session findSessionByAccessIdInternal(String accessId);

    protected abstract Session findSessionBySessionIdInternal(String sessionId);

    protected abstract Session destroySessionInternal(String sessionId);

    private void reinitialize(final Session session) {
        if (session != null) {
            final SerializableSessionImpl s = (SerializableSessionImpl) session;

            if (s.isRoot()) {
                s.reinitializeSessions();
            } else {
                ((SerializableSessionImpl) s.getRootSession()).reinitializeSessions();
            }
        }
    }

    public final Set<Session> findSessionsByPrincipal(final String principalName) {
        final Set<Session> sessions = findSessionsByPrincipalInternal(principalName);

        for (final Session session : sessions) {
            reinitialize(session);
        }

        return sessions;
    }

    public final Session findSessionByAccessId(final String accessId) {
        final Session session = findSessionByAccessIdInternal(accessId);
        reinitialize(session);
        return session;
    }

    public final Session findSessionBySessionId(final String sessionId) {
        final Session session = findSessionBySessionIdInternal(sessionId);
        reinitialize(session);

        return session;
    }

    public final Session destroySession(final String sessionId) {
        final Session session = destroySessionInternal(sessionId);
        reinitialize(session);
        return session;
    }
}
