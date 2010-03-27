package org.jasig.cas.server.authentication;

import java.net.URL;

/**
 * Represents a credential that is a provided URL.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface UrlCredential extends Credential {

    /**
     * The URL that represents the credential.  In general, it should be a secure URL, such as https, that a certificate
     * can be validated.
     *
     * @return the url.  CANNOT be NULL.
     */
    public URL getUrl();
    
}
