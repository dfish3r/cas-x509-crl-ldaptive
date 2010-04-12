package org.jasig.cas.server.authentication;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Default, simple, implementation of the {@link org.jasig.cas.server.authentication.UrlCredential}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5.0
 */
public final class DefaultUrlCredentialImpl implements UrlCredential {

    @NotNull
    private final URL url;

    public DefaultUrlCredentialImpl(final URL url) {
        this.url = url;
    }

    public DefaultUrlCredentialImpl(final String url) {
        try {
            this.url = new URL(url);
        } catch (final MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }

    }

    public URL getUrl() {
        return this.url;
    }
}
