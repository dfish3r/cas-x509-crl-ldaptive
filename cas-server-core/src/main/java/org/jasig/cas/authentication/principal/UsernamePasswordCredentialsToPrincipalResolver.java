/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.ja-sig.org/products/cas/overview/license/
 */
package org.jasig.cas.authentication.principal;

import org.jasig.cas.server.authentication.AttributePrincipalFactory;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.authentication.UserNamePasswordCredential;

/**
 * Implementation of CredentialsToPrincipalResolver for Credentials based on
 * UsernamePasswordCredentials when a SimplePrincipal (username only) is
 * sufficient.
 * <p>
 * Implementation extracts the username from the Credentials provided and
 * constructs a new SimplePrincipal with the unique id set to the username.
 * </p>
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.2 $ $Date: 2007/01/22 20:35:26 $
 * @since 3.0
 * @see org.jasig.cas.server.authentication.AttributePrincipal
 */
public final class UsernamePasswordCredentialsToPrincipalResolver extends AbstractPersonDirectoryCredentialsToPrincipalResolver {

    public UsernamePasswordCredentialsToPrincipalResolver(final AttributePrincipalFactory attributePrincipalFactory) {
        super(attributePrincipalFactory);
    }

    protected String extractPrincipalId(final Credential credentials) {
        final UserNamePasswordCredential usernamePasswordCredentials = (UserNamePasswordCredential) credentials;
        return usernamePasswordCredentials.getUserName();
    }

    /**
     * Return true if Credentials are UsernamePasswordCredentials, false
     * otherwise.
     */
    public boolean supports(final Credential credentials) {
        return credentials != null && UserNamePasswordCredential.class.isAssignableFrom(credentials.getClass());
    }
}
