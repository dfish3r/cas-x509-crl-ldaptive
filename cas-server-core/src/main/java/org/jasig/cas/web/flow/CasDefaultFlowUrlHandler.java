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

package org.jasig.cas.web.flow;

import org.springframework.webflow.context.servlet.DefaultFlowUrlHandler;

import javax.servlet.http.HttpServletRequest;

/**
 * Extends the {@link org.springframework.webflow.context.servlet.DefaultFlowUrlHandler} to support the CAS requirement
 * that tokens be retrieved via the "lt" request parameter.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.4
 */
public class CasDefaultFlowUrlHandler extends DefaultFlowUrlHandler {

    @Override
    public String getFlowExecutionKey(final HttpServletRequest request) {
        return request.getParameter("lt");
    }
}
