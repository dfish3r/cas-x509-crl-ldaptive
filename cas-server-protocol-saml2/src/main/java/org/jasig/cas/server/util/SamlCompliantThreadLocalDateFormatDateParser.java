package org.jasig.cas.server.util;

import org.jasig.cas.util.ThreadLocalDateFormatDateParser;

/**
 * Creates a {@link org.jasig.cas.server.util.DateParser} that's compliant with the SAML2 specification.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class SamlCompliantThreadLocalDateFormatDateParser extends ThreadLocalDateFormatDateParser {

    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss'Z'";

    public SamlCompliantThreadLocalDateFormatDateParser() {
        super(DATE_FORMAT);
    }
}
