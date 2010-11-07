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

package org.jasig.cas.server.web;

import org.jasig.cas.server.CentralAuthenticationService;
import org.jasig.cas.server.login.CasServiceAccessRequestImpl;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.jasig.cas.server.login.ServiceAccessResponse;
import org.jasig.cas.server.session.AccessException;
import org.jasig.cas.server.session.DefaultAccessResponseRequestImpl;
import org.jasig.cas.server.session.SessionException;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@Controller
public final class ProxyController {

    private final CentralAuthenticationService centralAuthenticationService;

    @Inject
    public ProxyController(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    @RequestMapping(method= RequestMethod.GET, value="/proxy")
    public void generateProxyRequest(@RequestParam(value="pgt") final String pgt, @RequestParam(value="targetService") final String service, final HttpServletRequest request, final Writer writer) throws IOException {
        final ServiceAccessRequest serviceAccessRequest = new CasServiceAccessRequestImpl(pgt, request.getRemoteAddr(), false, false, service, false);

        if (!StringUtils.hasText(pgt) || !StringUtils.hasText(service)) {
            writeErrorResponse("INVALID_REQUEST", "'pgt' and 'targetService' parameters are both required", writer);
            return;
        }

        try {
            final ServiceAccessResponse serviceAccessResponse = this.centralAuthenticationService.grantAccess(serviceAccessRequest);
            serviceAccessResponse.getAccess().generateResponse(new DefaultAccessResponseRequestImpl(writer, null, null));
            writer.flush();
        } catch (final SessionException e) {
            writeErrorResponse("BAD_PGT", e.getMessage(), writer);
        } catch (final AccessException e) {
            writeErrorResponse("INTERNAL_ERROR", e.getMessage(), writer);
        }
    }

    protected void writeErrorResponse(final String errorCode, final String errorMessage, final Writer writer) {
        final Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("errorCode", errorCode);
        parameters.put("errorMessage", errorMessage);
//        FreemarkerUtils.writeToFreeMarkerTemplate("CAS2proxyErrorResponseTemplate.ftl", parameters, writer);
    }

}
