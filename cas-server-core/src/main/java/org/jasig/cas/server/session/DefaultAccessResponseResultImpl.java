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
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultAccessResponseResultImpl implements AccessResponseResult {

    public static final AccessResponseResult NONE = new DefaultAccessResponseResultImpl(Operation.NONE, null, Collections.<String, List<String>>emptyMap());

    private final Operation operation;

    private final Map<String, List<String>> parameters;

    private final String url;

    private final String viewName;

    private final String contentType;

    private final String code;

    private final String messageCode;

    private final Map<String, Object> modelMap;

    private DefaultAccessResponseResultImpl(final String contentType) {
        this.viewName = null;
        this.operation = Operation.NONE;
        this.contentType = contentType;
        this.modelMap = Collections.emptyMap();
        this.messageCode = null;
        this.code = null;
        this.url = null;
        this.parameters = null;
    }

    private DefaultAccessResponseResultImpl(final String viewName, final Map<String, Object> modelMap) {
        this(viewName, null, modelMap);
    }

    private DefaultAccessResponseResultImpl(final String viewName, final String contentType, final Map<String, Object> modelMap) {
        this.viewName = viewName;
        this.operation = Operation.VIEW;
        this.contentType = contentType;
        this.messageCode = null;
        this.code = null;
        this.url = null;
        this.parameters = Collections.emptyMap();
        this.modelMap = Collections.unmodifiableMap(modelMap);
    }

    private DefaultAccessResponseResultImpl(final String viewName, final String code, final String messageCode, final String contentType) {
        this.viewName = viewName;
        this.code = code;
        this.messageCode = messageCode;
        this.operation = Operation.ERROR_VIEW;
        this.contentType = contentType;
        this.parameters = Collections.emptyMap();
        this.url = null;
        this.modelMap = Collections.emptyMap();
    }

    private DefaultAccessResponseResultImpl(final Operation operation, final String url, final Map<String, List<String>> params) {
        this.operation = operation;
        this.url =  url;
        this.parameters = Collections.unmodifiableMap(params);
        this.viewName = null;
        this.contentType = null;
        this.code = null;
        this.messageCode = null;
        this.modelMap = Collections.emptyMap();
    }

    public static AccessResponseResult none(final String contentType) {
        return new DefaultAccessResponseResultImpl(contentType);
    }

    public static AccessResponseResult generateRedirect(final String url) {
        return generateRedirect(url, Collections.<String, List<String>>emptyMap());
    }

    public static AccessResponseResult generateRedirect(final String url, final Map<String, List<String>> params) {
        return new DefaultAccessResponseResultImpl(Operation.REDIRECT, url, params);
    }

    public static AccessResponseResult generatePostRedirect(final String url) {
        return generatePostRedirect(url, Collections.<String, List<String>>emptyMap());
    }

    public static AccessResponseResult generatePostRedirect(final String url, final Map<String, List<String>> params) {
        return new DefaultAccessResponseResultImpl(Operation.POST, url, params);
    }

    public static AccessResponseResult generateView(final String viewName, final Map<String, Object> modelMap) {
        return generateView(viewName, null, modelMap);
    }

    public static AccessResponseResult generateView(final String viewName, final String contentType, final Map<String, Object> modelMap) {
        return new DefaultAccessResponseResultImpl(viewName, contentType, modelMap);
    }

    public static AccessResponseResult generateErrorView(final String viewName, final String code, final String message) {
        return generateErrorView(viewName, null, code, message);
    }

    public static AccessResponseResult generateErrorView(final String viewName, final String contentType, final String code, final String message) {
        return new DefaultAccessResponseResultImpl(viewName, code, message, contentType);
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

            final String[] splitFragment = this.url.split("#");

            stringBuilder.append(splitFragment[0]);

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

            if (splitFragment.length > 1) {
                stringBuilder.append("#");
                stringBuilder.append(splitFragment[1]);
            }

            return stringBuilder.toString();
        } else {
            return this.url;
        }
    }

    public Map<String, List<String>> getParameters() {
        return this.parameters;
    }

    public String getViewName() {
        return this.viewName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessageCode() {
        return this.messageCode;
    }

    public Map<String, Object> getModelMap() {
        return this.modelMap;
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
