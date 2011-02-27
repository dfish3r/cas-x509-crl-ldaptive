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

import org.jasig.cas.server.login.ServiceAccessRequest;
import org.jasig.cas.server.util.Saml1UniqueTicketIdGeneratorImpl;

/**
 * Knows how to create JPA-backed SAML1.1 accesses.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class JpaSaml11ProtocolAccessImplFactory extends AbstractSaml1ProtocolAccessImplFactory {

    public JpaSaml11ProtocolAccessImplFactory(final Saml1UniqueTicketIdGeneratorImpl uniqueTicketIdGenerator, final String issuer) {
        super(uniqueTicketIdGenerator, issuer);
    }

    public Access getAccess(final Session session, final ServiceAccessRequest serviceAccessRequest) {
        AbstractStaticSaml1ProtocolAccessImpl.setExpirationPolicy(getExpirationPolicy());
        return new JpaSaml11ProtocolAccessImpl(getUniqueTicketIdGenerator().getNewTicketId(""), session, serviceAccessRequest, getIssuer(), getIssueLength());
    }
}
