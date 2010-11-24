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

import org.jasig.cas.server.util.UniqueTicketIdGenerator;

import javax.validation.constraints.NotNull;

/**
 * Abstract SAMl1 access factory that provides some required attributes.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSaml1ProtocolAccessImplFactory implements AccessFactory {

    private static final String DEFAULT_ENCODING = "UTF-8";

    @NotNull
    private final UniqueTicketIdGenerator uniqueTicketIdGenerator;

    @NotNull
    private String encoding = DEFAULT_ENCODING;

    protected AbstractSaml1ProtocolAccessImplFactory(final UniqueTicketIdGenerator uniqueTicketIdGenerator) {
        this.uniqueTicketIdGenerator = uniqueTicketIdGenerator;
    }

    protected UniqueTicketIdGenerator getUniqueTicketIdGenerator() {
        return this.uniqueTicketIdGenerator;
    }

    public final void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    protected final String getEncoding() {
        return this.encoding;
    }


}
