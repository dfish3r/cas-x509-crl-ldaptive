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
import org.jasig.cas.server.authentication.UrlCredential;
import org.jasig.cas.server.util.CasProtocolUniqueTicketIdGeneratorImpl;
import org.jasig.cas.server.util.UniqueTicketIdGenerator;
import org.jasig.cas.util.HttpClient;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 */
public final class DefaultProxyHandlerImpl implements ProxyHandler {

    @NotNull
    private UniqueTicketIdGenerator uniqueTicketIdGenerator = new CasProtocolUniqueTicketIdGeneratorImpl();

    @NotNull
    private HttpClient httpClient = new HttpClient();

    public String handleProxyGrantingRequest(final String proxySessionId, final Credential credential) {

        if (!(credential instanceof UrlCredential)) {
            return null;
        }

        final UrlCredential urlCredential = (UrlCredential) credential;
        final String url = urlCredential.getUrl().toExternalForm();

        final String proxyGrantingTicketIou = this.uniqueTicketIdGenerator.getNewTicketId("PGTIOU");

        final StringBuilder stringBuffer = new StringBuilder(
            url.length() + proxyGrantingTicketIou.length()
                + proxySessionId.length() + 15);

        stringBuffer.append(url);

        if (url.indexOf("?") != -1) {
            stringBuffer.append("&");
        } else {
            stringBuffer.append("?");
        }

        stringBuffer.append("pgtIou=");
        stringBuffer.append(proxyGrantingTicketIou);
        stringBuffer.append("&pgtId=");
        stringBuffer.append(proxySessionId);

        try {
            if (this.httpClient.isValidEndPoint(new URL(stringBuffer.toString()))) {
                return proxyGrantingTicketIou;
            }
        } catch (final MalformedURLException e) {
            // nothing to do
        }

        return null;
    }

    public void setUniqueTicketIdGenerator(final UniqueTicketIdGenerator uniqueTicketIdGenerator) {
        this.uniqueTicketIdGenerator = uniqueTicketIdGenerator;
    }

    public void setHttpClient(final HttpClient httpClient) {
        this.httpClient = httpClient;
    }
}
