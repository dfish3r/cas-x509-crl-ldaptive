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
 * Default implementation of the {@link org.jasig.cas.server.session.AccessResponseRequest}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class DefaultAccessResponseRequestImpl implements AccessResponseRequest {

    private final Writer writer;

    private final String proxySessionId;

    private final Credential credential;

    public DefaultAccessResponseRequestImpl(final Writer writer, final String proxySessionId, final Credential credential) {
        this.writer = writer;
        this.proxySessionId = proxySessionId;
        this.credential = credential;
    }

    public final Writer getWriter() {
        return this.writer;
    }

    public final String getProxySessionId() {
        return this.proxySessionId;
    }

    public final Credential getProxiedCredential() {
        return this.credential;
    }
}
