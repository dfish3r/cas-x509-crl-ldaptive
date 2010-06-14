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
package org.jasig.cas.adaptors.trusted.authentication.principal;

import org.jasig.cas.server.authentication.AbstractAttributePrincipalFactoryCredentialsToPrincipalResolver;
import org.jasig.cas.server.authentication.AttributePrincipalFactory;
import org.jasig.cas.server.authentication.Credential;

/**
 * Extracts the Principal out of PrincipalBearingCredentials. It is very simple
 * to resolve PrincipalBearingCredentials to a Principal since the credentials
 * already bear the ready-to-go Principal.
 * 
 * @author Andrew Petro
 * @version $Revision$ $Date$
 * @since 3.0.5
 */
public final class PrincipalBearingCredentialsToPrincipalResolver extends AbstractAttributePrincipalFactoryCredentialsToPrincipalResolver {

    public PrincipalBearingCredentialsToPrincipalResolver(final AttributePrincipalFactory attributePrincipalFactory) {
        super(attributePrincipalFactory);
    }

    protected String extractPrincipalId(final Credential credentials) {
        return ((PrincipalBearingCredentials) credentials).getPrincipal().getName();
    }

    public boolean supports(final Credential credentials) {
        return credentials != null && credentials.getClass().equals(PrincipalBearingCredentials.class);
    }
}