package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.Credential;

import java.io.Writer;

/**
 * Represents a generic request for a response.  There should be specific protocol versions of each.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public interface AccessResponseRequest {

    /**
     * A generic writer that a response can be written to instead of actually generating a response.
     *
     * @return the writer.
     */
    Writer getWriter();

    /**
     * Returns the identifier for a proxy session if one is to be created.
     *
     * @return the proxy session id.
     * // TODO does this make sense?
     */
    String getProxySessionId();

    /**
     * Returns the credentials used to create the proxy session.
     *
     * @return the credentials used to create the proxy session, or null.
     * // TODO does this make sense?
     */
    Credential getProxiedCredential();
}

