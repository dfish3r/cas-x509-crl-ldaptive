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

package org.jasig.cas.util;

import org.opensaml.artifact.SAMLArtifactType0001;
import org.opensaml.artifact.SAMLArtifactType0002;
import org.opensaml.artifact.URI;

import javax.validation.constraints.NotNull;

/**
 * Unique Ticket Id Generator compliant with the SAML 1.1 specification for
 * artifacts. This should also be compliant with the SAML 2 specification.
 * <p>
 * Default to SAML 1.1 Compliance.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class SamlCompliantUniqueTicketIdGenerator implements UniqueTicketIdGenerator {

    /** SAML defines the source id as the server name. */
    @NotNull
    private final String sourceLocation;

    private boolean saml2compliant;

    /** Random generator to construct the AssertionHandle. */
    private final RandomStringGenerator randomStringGenerator = new DefaultRandomStringGenerator(20);

    public SamlCompliantUniqueTicketIdGenerator(final String sourceId) {
        this.sourceLocation = sourceId;
    }

    /**
     * We ignore prefixes for SAML compliance.
     */
    public String getNewTicketId(final String prefix) {
        if (saml2compliant) {
            return new SAMLArtifactType0002(this.randomStringGenerator.getNewStringAsBytes(), new URI(this.sourceLocation)).encode();
        } else {
            return new SAMLArtifactType0001(this.randomStringGenerator.getNewStringAsBytes(), new URI(this.sourceLocation).toBytes()).encode();
        }
    }

    public void setSaml2compliant(final boolean saml2compliant) {
        this.saml2compliant = saml2compliant;
    }
}
