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

import java.util.Map;
import java.util.List;
import java.util.Collections;
import java.util.HashMap;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultAccessResponseResultImpl implements AccessResponseResult {

    public static final AccessResponseResult NONE = new DefaultAccessResponseResultImpl(AccessResponseResult.Operation.NONE, new HashMap<String, List<String>>(), null);

    private final Operation operation;

    private final Map<String, List<String>> parameters;

    private final String url;

    public DefaultAccessResponseResultImpl(final AccessResponseResult.Operation operation, final Map<String, List<String>> params, final String url) {
        this.operation = operation;
        this.url =  url;
        this.parameters = Collections.unmodifiableMap(params);
    }

    public Operation getOperationToPerform() {
        return this.operation;
    }

    public String getUrl() {
        if (getOperationToPerform() == Operation.REDIRECT) {
            if (parameters.isEmpty()) {
                return this.url;
            }

            final StringBuilder stringBuilder = new StringBuilder();
            boolean firstParam = true;

            stringBuilder.append(this.url);

            for (final Map.Entry<String, List<String>> entry : getParameters().entrySet()) {
                final String keyValues = parseEntriesForItem(entry.getKey(), entry.getValue());

                if (firstParam) {
                    stringBuilder.append("?");
                    firstParam = false;
                } else {
                    stringBuilder.append("&");
                }

                stringBuilder.append(keyValues);
            }

            return stringBuilder.toString();
        } else {
            return this.url;
        }
    }

    public Map<String, List<String>> getParameters() {
        return this.parameters;
    }

    private String parseEntriesForItem(final String key, final List<String> values) {
        if (values.size() == 1) {
            return key + "=" + getEncodedValue(values.get(0));
        }

        final StringBuilder stringBuilder = new StringBuilder();

        for (final String value : values) {
            stringBuilder.append(key);
            stringBuilder.append("=");
            stringBuilder.append(getEncodedValue(value));
        }
        final String keyValue = stringBuilder.toString();
        return keyValue.substring(0, keyValue.length() - 1);
    }

    private String getEncodedValue(final String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (final UnsupportedEncodingException e) {
            return value;
        }
    }
}
