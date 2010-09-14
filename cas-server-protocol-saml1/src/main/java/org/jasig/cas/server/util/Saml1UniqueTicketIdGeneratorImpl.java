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
