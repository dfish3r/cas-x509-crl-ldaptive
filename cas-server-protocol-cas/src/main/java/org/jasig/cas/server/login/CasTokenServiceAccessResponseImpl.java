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

package org.jasig.cas.server.login;

import org.jasig.cas.server.session.*;

import java.util.*;

/**
 * Generates a {@link ServiceAccessResponse} that can handle {@link TokenServiceAccessRequest}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class CasTokenServiceAccessResponseImpl extends DefaultLoginResponseImpl implements ServiceAccessResponse {

    private static final String VIEW_NAME = "casServiceFailureView";

    private final Access access;

    private final CasTokenServiceAccessRequestImpl casTokenServiceAccessRequest;

    public CasTokenServiceAccessResponseImpl(final TokenServiceAccessRequest tokenServiceAccessRequest) {
        this.casTokenServiceAccessRequest = (CasTokenServiceAccessRequestImpl) tokenServiceAccessRequest;
        this.access = null;
    }

    public CasTokenServiceAccessResponseImpl(final Access access) {
        this.access = access;
        this.casTokenServiceAccessRequest = null;
    }

    public List<Access> getLoggedOutAccesses() {
        return Collections.emptyList();
    }

    public AccessResponseResult generateResponse(final AccessResponseRequest accessResponseRequest) {
        if (access != null) {
            return this.access.generateResponse(accessResponseRequest);
        }

        final String code;
        final String messageCode;
        if (casTokenServiceAccessRequest != null && !casTokenServiceAccessRequest.IsValid()) {
            code = "INVALID_REQUEST";
            messageCode = "INVALID_REQUEST";
        } else {
            code = "INVALID_TICKET";
            messageCode = "INVALID_TICKET";
        }

        // no access
        return DefaultAccessResponseResultImpl.generateErrorView(VIEW_NAME, code, messageCode);
    }
}
