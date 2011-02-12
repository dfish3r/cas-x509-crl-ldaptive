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
import org.jasig.cas.server.util.DefaultServiceIdentifierMatcherImpl;
import org.jasig.cas.server.util.ServiceIdentifierMatcher;
import org.jasig.cas.server.util.UniqueTicketIdGenerator;

import javax.validation.constraints.NotNull;

/**
 * Abstract class to handle the basic loading of collaborators that any implementation of the CasProtocol will
 * need.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public abstract class AbstractCasProtocolAccessImplFactory implements AccessFactory {

    @NotNull
    private ServiceIdentifierMatcher serviceIdentifierMatcher = new DefaultServiceIdentifierMatcherImpl();

    @NotNull
    private ProxyHandler proxyHandler = new DefaultProxyHandlerImpl();

    @NotNull
    // normally 10000
    private ExpirationPolicy expirationPolicy = new MultiUseOrTimeToLiveExpirationPolicy(1, 1000000);

    @NotNull
    private UniqueTicketIdGenerator uniqueTicketIdGenerator = new CasProtocolUniqueTicketIdGeneratorImpl();

    public final void setServiceIdentifierMatcher(final ServiceIdentifierMatcher serviceIdentifierMatcher) {
        this.serviceIdentifierMatcher = serviceIdentifierMatcher;
    }

    public final void setProxyHandler(final ProxyHandler proxyHandler) {
        this.proxyHandler = proxyHandler;
    }

    protected final ServiceIdentifierMatcher getServiceIdentifierMatcher() {
        return serviceIdentifierMatcher;
    }

    protected final ProxyHandler getProxyHandler() {
        return proxyHandler;
    }

    protected final ExpirationPolicy getExpirationPolicy() {
        return this.expirationPolicy;
    }

    public final void setExpirationPolicy(final ExpirationPolicy expirationPolicy) {
        this.expirationPolicy = expirationPolicy;
    }

    public final void setUniqueTicketIdGenerator(final UniqueTicketIdGenerator uniqueTicketIdGenerator) {
        this.uniqueTicketIdGenerator = uniqueTicketIdGenerator;
    }

    protected final UniqueTicketIdGenerator getUniqueTicketIdGenerator() {
        return this.uniqueTicketIdGenerator;
    }
}