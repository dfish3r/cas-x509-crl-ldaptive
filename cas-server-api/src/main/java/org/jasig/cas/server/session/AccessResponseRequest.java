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

