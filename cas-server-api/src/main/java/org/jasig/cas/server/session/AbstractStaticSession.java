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

/**
 * Abstract class to help implementations of the {@link org.jasig.cas.server.session.SessionStorage} interface
 * that don't store object in memory (and thus don't want to serialize some stuff).
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractStaticSession extends AbstractSession {

    private static ExpirationPolicy EXPIRATION_POLICY;

    private static List<AccessFactory> ACCESS_FACTORIES;

    public static void setExpirationPolicy(final ExpirationPolicy expirationPolicy) {
        EXPIRATION_POLICY = expirationPolicy;
    }

    public static void setAccessFactories(final List<AccessFactory> accessFactories) {
        ACCESS_FACTORIES = accessFactories;
    }

    protected final List<AccessFactory> getAccessFactories() {
        return ACCESS_FACTORIES;
    }

    protected final ExpirationPolicy getExpirationPolicy() {
        return EXPIRATION_POLICY;
    }
}
