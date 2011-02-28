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

import org.jasig.cas.server.login.Saml11RequestAccessRequestImpl;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.jasig.cas.server.util.Saml1UniqueTicketIdGeneratorImpl;

import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Creates new instances of the Serializable SAML object.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
@Named("saml11ProtocolAccessFactory")
@Protocol(Protocol.ProtocolType.SAML11)
@Singleton
public final class SerializableSaml11ProtocolAccessImplFactory extends AbstractSaml1ProtocolAccessImplFactory {

    public SerializableSaml11ProtocolAccessImplFactory(final Saml1UniqueTicketIdGeneratorImpl uniqueTicketIdGenerator, final String issuer) {
        super(uniqueTicketIdGenerator, issuer);
    }

    public Access getAccess(final Session session, final ServiceAccessRequest serviceAccessRequest) {
        if (!(serviceAccessRequest instanceof Saml11RequestAccessRequestImpl)) {
            return null;
        }

        final Saml11RequestAccessRequestImpl impl = (Saml11RequestAccessRequestImpl) serviceAccessRequest;
        return new SerializableSaml11ProtocolAccessImpl(getUniqueTicketIdGenerator().getNewTicketId(""), session, impl, getIssuer(), getIssueLength());
    }
}
