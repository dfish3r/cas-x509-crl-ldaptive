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

import java.util.List;
import java.util.Map;

/**
 * Represents the result of an {@link org.jasig.cas.server.session.Access#generateResponse(AccessResponseRequest)}
 * attempt. Notifies the higher layers whether there is any action they need to take (i.e. REDIRECT, POST, etc.)
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface AccessResponseResult {

    enum Operation {REDIRECT, POST, ERROR_VIEW, VIEW, NONE}

    /**
     * The operation that the higher level should perform based on this result.
     *
     * @return the operation.  Cannot be null.
     */
    Operation getOperationToPerform();

    /**
     * The URL that we should be operating on if the operation is either a REDIRECT or a POST.
     * @return the String.  CAN be null ONLY if the Operation is NONE.
     */
    String getUrl();

    /**
     * The parameters to return if a POST is issued.  For REDIRECT, they should be encoded in the url.
     * @return the parameters map.  CAN be empty but NOT null.
     */
    Map<String, List<String>> getParameters();

    /**
     * The name of the view to use when constructing this response.
     *
     * @return the view.  CANNOT be NULL if the Operation is VIEW.
     */
    String getViewName();

    /**
     * Returns the Model Map.  MUST only be used when the operation type is VIEW.
     *
     * @return the model map.  CANNOT be NULL if operation type is VIEW.
     */
    Map<String, Object> getModelMap();

    /**
     * Sets the content-type to be sent to the client.
     *
     * @return the content type.  Can be null.
     */
    String getContentType();

    /**
     * The error code used when the {@link #getOperationToPerform()} is {@link Operation#ERROR_VIEW}.
     *
     * @return error code.  CANNOT be NULL.
     */
    String getCode();

    /**
     * The error code used when the {@link #getOperationToPerform()} is {@link Operation#ERROR_VIEW}.
     *
     * @return message code.  CANNOT be NULL.
     */

    String getMessageCode();
}
