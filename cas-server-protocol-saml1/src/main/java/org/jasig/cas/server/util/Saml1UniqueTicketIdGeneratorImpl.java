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

package org.jasig.cas.server.util;

import org.jasig.cas.util.DefaultRandomStringGenerator;
import org.opensaml.artifact.SAMLArtifactType0001;

import java.security.MessageDigest;

/**
 * SAML1-compliant unique ticket id generator.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class Saml1UniqueTicketIdGeneratorImpl implements UniqueTicketIdGenerator {

       /** SAML defines the source id as the server name. */
    private final byte[] sourceIdDigest;

    /** Random generator to construct the AssertionHandle. */
    private final RandomStringGenerator randomStringGenerator = new DefaultRandomStringGenerator(20);

    public Saml1UniqueTicketIdGeneratorImpl(final String sourceId) {
        try {
            final MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            messageDigest.update(sourceId.getBytes("8859_1"));
            this.sourceIdDigest = messageDigest.digest();
        } catch (final Exception e) {
            throw new IllegalStateException("Exception generating digest which should not happen...EVER");
        }
    }

    /**
     * We ignore prefixes for SAML compliance.
     */
    public String getNewTicketId(final String prefix) {
        return new SAMLArtifactType0001(this.sourceIdDigest, this.randomStringGenerator.getNewStringAsBytes()).encode();
    }
}
