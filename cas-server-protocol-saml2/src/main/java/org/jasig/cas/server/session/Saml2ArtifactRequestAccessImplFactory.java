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

import org.jasig.cas.server.login.Saml2ArtifactRequestAccessRequestImpl;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.jasig.cas.server.util.SamlCompliantThreadLocalDateFormatDateParser;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

/**
 * Constructs a SAML2 Artifact Request/Response.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@Named("saml2ArtifactRequestAccessFactory")
@Protocol(Protocol.ProtocolType.SAML2)
@Singleton
public final class Saml2ArtifactRequestAccessImplFactory implements AccessFactory {

    @NotNull
    private final String issuer;

    @NotNull
    private final SamlCompliantThreadLocalDateFormatDateParser dateParser;

    public Saml2ArtifactRequestAccessImplFactory(final String issuer) {
        this(issuer, new SamlCompliantThreadLocalDateFormatDateParser());
    }

    /**
     * Public constructor that takes the required issuer name (which most SPs will configure on their end to check
     *
     * @param issuer the name of the issuer.  CANNOT be NULL.
     * @param parser the SAML compliant date parser to use.
     */
    public Saml2ArtifactRequestAccessImplFactory(final String issuer, final SamlCompliantThreadLocalDateFormatDateParser parser) {
        this.issuer = issuer;
        this.dateParser = parser;
    }

    public Access getAccess(final Session session, final ServiceAccessRequest serviceAccessRequest) {
        if (!(serviceAccessRequest instanceof Saml2ArtifactRequestAccessRequestImpl)) {
            return null;
        }
        
        return new Saml2ArtifactRequestAccessImpl(session, (Saml2ArtifactRequestAccessRequestImpl) serviceAccessRequest, this.issuer, this.dateParser);
    }
}
