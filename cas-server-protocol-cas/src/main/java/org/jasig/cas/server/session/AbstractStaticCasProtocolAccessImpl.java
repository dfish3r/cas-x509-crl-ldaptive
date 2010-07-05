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

import org.jasig.cas.server.util.CasProtocolUniqueTicketIdGeneratorImpl;
import org.jasig.cas.server.util.ServiceIdentifierMatcher;
import org.jasig.cas.server.util.UniqueTicketIdGenerator;

/**
 * Abstract implementation that relies on static properties for implementations that persist data to another data storage and don't want to persist the
 * actual collaborators.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public abstract class AbstractStaticCasProtocolAccessImpl extends AbstractCasProtocolAccessImpl {

    private static ProxyHandler PROXY_HANDLER;

    private static ServiceIdentifierMatcher SERVICE_IDENTIFIER_MATCHER;

    private static ExpirationPolicy EXPIRATION_POLICY;

    private static UniqueTicketIdGenerator ID_GENERATOR = new CasProtocolUniqueTicketIdGeneratorImpl();

    protected final ServiceIdentifierMatcher getServiceIdentifierMatcher() {
        return SERVICE_IDENTIFIER_MATCHER;
    }

    protected final ProxyHandler getProxyHandler() {
        return PROXY_HANDLER;
    }

    public static void setProxyHandler(final ProxyHandler proxyHandler) {
        PROXY_HANDLER = proxyHandler;
    }

    public static void setServiceIdentifierMatcher(final ServiceIdentifierMatcher serviceIdentifierMatcher) {
        SERVICE_IDENTIFIER_MATCHER = serviceIdentifierMatcher;
    }

    public static void setExpirationPolicy(final ExpirationPolicy expirationPolicy) {
        EXPIRATION_POLICY = expirationPolicy;
    }

    public static void setUniqueTicketIdGenerator(final UniqueTicketIdGenerator uniqueTicketIdGenerator) {
        ID_GENERATOR = uniqueTicketIdGenerator;
    }

    @Override
    protected final UniqueTicketIdGenerator getIdGenerator() {
        return ID_GENERATOR;
    }

    @Override
    protected final ExpirationPolicy getExpirationPolicy() {
        return EXPIRATION_POLICY;
    }
}
