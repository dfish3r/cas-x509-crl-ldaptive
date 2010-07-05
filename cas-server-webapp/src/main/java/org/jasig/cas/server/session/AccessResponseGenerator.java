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

package org.jasig.cas.server.session;

import org.jasig.cas.server.login.ServiceAccessResponse;
import org.springframework.webflow.context.ExternalContext;

import java.io.Writer;

/**
 * Retrieves the {@link org.jasig.cas.server.login.ServiceAccessResponse} from the Access.  Required class because Spring Web Flow
 * can't construct "new" Objects in the flow (as far as I can tell).
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class AccessResponseGenerator {

    public AccessResponseResult generateAccessResponseResult(final ExternalContext externalContext, final ServiceAccessResponse serviceAccessResponse) {
        final Writer writer = externalContext.getResponseWriter();
        final AccessResponseRequest accessResponseRequest = new DefaultAccessResponseRequestImpl(writer, null, null);
        return serviceAccessResponse.getAccess().generateResponse(accessResponseRequest);
    }
}
