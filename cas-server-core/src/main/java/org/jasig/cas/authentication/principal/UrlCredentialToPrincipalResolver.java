/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.authentication.principal;

import org.jasig.cas.server.authentication.*;

import javax.validation.constraints.NotNull;

/**
 * UrlCredentialToPrincipalResolver extracts the callbackUrl from
 * the HttpBasedServiceCredentials and constructs a SimpleService with the
 * callbackUrl as the unique Id.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.5 $ $Date: 2007/02/27 19:31:58 $
 * @since 3.0
 */
public final class UrlCredentialToPrincipalResolver implements CredentialToPrincipalResolver {

    @NotNull
    private final AttributePrincipalFactory attributePrincipalFactory;

    public UrlCredentialToPrincipalResolver(final AttributePrincipalFactory attributePrincipalFactory) {
        this.attributePrincipalFactory = attributePrincipalFactory;
    }

    /**
     * Method to return a simple Service Principal with the identifier set to be
     * the callback url.
     */
    public AttributePrincipal resolve(final Credential credentials) {
        final UrlCredential serviceCredentials = (UrlCredential) credentials;
        return this.attributePrincipalFactory.getAttributePrincipal(serviceCredentials.getUrl().toExternalForm());
    }

    /**
     * @return true if the credentials provided are not null and are assignable
     * from HttpBasedServiceCredentials, otherwise returns false.
     */
    public boolean supports(final Credential credentials) {
        return credentials != null
            && UrlCredential.class.isAssignableFrom(credentials
                .getClass());
    }
}
