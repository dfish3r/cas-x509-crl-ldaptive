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

import org.jasig.cas.server.session.Access;
import org.jasig.cas.server.session.AccessResponseRequest;
import org.jasig.cas.server.session.AccessResponseResult;

import java.util.Collections;
import java.util.List;

/**
 *
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class Saml11RequestAccessResponseImpl extends DefaultLoginResponseImpl implements ServiceAccessResponse {

    private final Access access;

    private final Saml11RequestAccessRequestImpl serviceAccessRequest;

    public Saml11RequestAccessResponseImpl(final Access access) {
        super(null, null);
        this.access = access;
        this.serviceAccessRequest = null;
    }

    public Saml11RequestAccessResponseImpl(final Saml11RequestAccessRequestImpl serviceAccessRequest) {
        super(null, null);
        this.serviceAccessRequest = serviceAccessRequest;
        this.access = null;
    }



    public List<Access> getLoggedOutAccesses() {
        return Collections.emptyList();
    }

    public AccessResponseResult generateResponse(final AccessResponseRequest accessResponseRequest) {
        if (this.access != null) {
            return this.access.generateResponse(accessResponseRequest);
        }

        // what other responses can we have?
        throw new IllegalStateException("We don't know how to generate a response!");

    }
}
