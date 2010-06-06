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

import java.util.Date;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultLogoutRequestImpl implements LogoutRequest {

    private final Date date = new Date();

    private final String sessionId;

    public DefaultLogoutRequestImpl(final String sessionId) {
        this.sessionId = sessionId;
    }

    public Date getDate() {
        return new Date(date.getTime());
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public String toString() {
        return "sessionId=" + this.sessionId;
    }
}