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

package org.jasig.cas.server.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1
 *
 */
public abstract class AbstractAttributePrincipalFactoryCredentialsToPrincipalResolver implements CredentialToPrincipalResolver {

    /** Log instance. */
    protected final Logger log = LoggerFactory.getLogger(this.getClass());

    private boolean returnNullIfNoAttributes = false;

    @NotNull
    private final AttributePrincipalFactory attributePrincipalFactory;

    @NotNull
    private final Class<?> clazz;

    protected AbstractAttributePrincipalFactoryCredentialsToPrincipalResolver(final AttributePrincipalFactory attributePrincipalFactory, final Class<?> clazz) {
        this.attributePrincipalFactory = attributePrincipalFactory;
        this.clazz = clazz;
    }

    /**
     * @return true if the credentials provided are not null and are assignable
     * from {@link AbstractAttributePrincipalFactoryCredentialsToPrincipalResolver#clazz}, otherwise returns false.
     */
    public final boolean supports(final Credential credentials) {
        return credentials != null && clazz.isAssignableFrom(credentials.getClass());
    }

    public final AttributePrincipal resolve(final Credential credentials) {
        log.debug("Attempting to resolve a principal...");

        final String principalId = extractPrincipalId(credentials);
        
        if (principalId == null) {
            return null;
        }
        
        if (log.isDebugEnabled()) {
            log.debug(String.format("Creating SimplePrincipal for [%s]", principalId));
        }

        final AttributePrincipal attributePrincipal = this.attributePrincipalFactory.getAttributePrincipal(principalId);

        if (attributePrincipal.getAttributes().isEmpty() && this.returnNullIfNoAttributes) {
            return null;
        }
        
        return attributePrincipal;
    }
    
    /**
     * Extracts the id of the user from the provided credentials.
     * 
     * @param credentials the credentials provided by the user.
     * @return the username, or null if it could not be resolved.
     */
    protected abstract String extractPrincipalId(Credential credentials);

    public final void setReturnNullIfNoAttributes(final boolean returnNullIfNoAttributes) {
        this.returnNullIfNoAttributes = returnNullIfNoAttributes;
    }
}
