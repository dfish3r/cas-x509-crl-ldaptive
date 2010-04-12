/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.authentication.handler.support;

import org.jasig.cas.server.authentication.AbstractPreAndPostProcessingAuthenticationHandler;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.authentication.UrlCredential;
import org.jasig.cas.util.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;

/**
 * Class to validate the credentials presented by communicating with the web
 * server and checking the certificate that is returned against the hostname,
 * etc.
 * <p>
 * This class is concerned with ensuring that the protocol is HTTPS and that a
 * response is returned. The SSL handshake that occurs automatically by opening
 * a connection does the heavy process of authenticating.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public class HttpBasedServiceCredentialsAuthenticationHandler extends AbstractPreAndPostProcessingAuthenticationHandler {

    /** The string representing the HTTPS protocol. */
    private static final String PROTOCOL_HTTPS = "https";

    /** Boolean variable denoting whether secure connection is required or not. */
    private boolean requireSecure = true;

    /** Log instance. */
    private final Logger log = LoggerFactory.getLogger(getClass());

    /** Instance of Apache Commons HttpClient */
    @NotNull
    private HttpClient httpClient;

    @Override
    protected final boolean doAuthentication(final Credential credentials) throws GeneralSecurityException {
        final UrlCredential serviceCredentials = (UrlCredential) credentials;
        if (this.requireSecure
            && !serviceCredentials.getUrl().getProtocol().equals(PROTOCOL_HTTPS)) {
            if (log.isDebugEnabled()) {
                log.debug("Authentication failed because url was not secure.");
            }
            return false;
        }
        log
            .debug("Attempting to resolve credentials for "
                + serviceCredentials);

        return this.httpClient.isValidEndPoint(serviceCredentials.getUrl());
    }

    /**
     * @return true if the credentials provided are not null and the credentials
     * are a subclass of (or equal to) HttpBasedServiceCredentials.
     */
    public final boolean supports(final Credential credentials) {
        return credentials != null && UrlCredential.class.isAssignableFrom(credentials.getClass());
    }

    /** Sets the HttpClient which will do all of the connection stuff. */
    public final void setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    /**
     * Set whether a secure url is required or not.
     * 
     * @param requireSecure true if its required, false if not. Default is true.
     */
    public final void setRequireSecure(final boolean requireSecure) {
        this.requireSecure = requireSecure;
    }
}
