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

package org.jasig.cas.authentication.principal;

import java.net.URLEncoder;
import java.util.Map;

/**
 * Encapsulates a Response to send back for a particular service.
 * 
 * @author Scott Battaglia
 * @author Arnaud Lesueur
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1
 */
public final class Response {

    public static enum ResponseType {
        POST, REDIRECT
    }

    private final ResponseType responseType;

    private final String url;

    private final Map<String, String> attributes;

    protected Response(ResponseType responseType, final String url,
        final Map<String, String> attributes) {
        this.responseType = responseType;
        this.url = url;
        this.attributes = attributes;
    }

    public static Response getPostResponse(final String url,
        final Map<String, String> attributes) {
        return new Response(ResponseType.POST, url, attributes);
    }

    public static Response getRedirectResponse(final String url,
        final Map<String, String> parameters) {
        final StringBuilder builder = new StringBuilder(
            parameters.size() * 40 + 100);
        boolean isFirst = true;
        
        builder.append(url);
        
        for (final Map.Entry<String, String> entry : parameters.entrySet()) {
            if (entry.getValue() != null) {
                if (isFirst) {
                    builder.append(url.contains("?") ? "&" : "?");
                    isFirst = false;
                } else {
                    builder.append("&");   
                }
                builder.append(entry.getKey());
                builder.append("=");

                try {
                    builder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                } catch (final Exception e) {
                    builder.append(entry.getValue());
                }
            }
        }

        return new Response(ResponseType.REDIRECT, builder.toString(), parameters);
    }

    public Map<String, String> getAttributes() {
        return this.attributes;
    }

    public ResponseType getResponseType() {
        return this.responseType;
    }

    public String getUrl() {
        return this.url;
    }
}
