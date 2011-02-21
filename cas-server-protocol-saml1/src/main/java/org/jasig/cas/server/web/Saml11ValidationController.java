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
import org.jasig.cas.server.login.Saml1TokenServiceAccessRequestImpl;
import org.jasig.cas.server.login.ServiceAccessResponse;
import org.jasig.cas.server.login.TokenServiceAccessRequest;
import org.jasig.cas.server.session.AccessResponseRequest;
import org.jasig.cas.server.session.DefaultAccessResponseRequestImpl;
import org.opensaml.saml1.binding.decoding.HTTPSOAP11Decoder;
import org.opensaml.ws.message.MessageContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Writer;

/**
 * Validates a SAML 1.1
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
@Controller("saml11ValidationController")
public final class Saml11ValidationController {

    private static final String DEFAULT_ENCODING = "UTF-8";

    private static final String HEADER_SOAP_ACTION = "SOAPAction";

    private static final String HEADER_SOAP_ACTION_VALUE = "http://www.oasis-open.org/committees/security";

    private static final String SOAP_ENV_BODY = "<SOAP-ENV:Body>";

    private static final String SOAP_ENV_BODY_END = "</SOAP-ENV:Body>";

    private final CentralAuthenticationService centralAuthenticationService;

    private String encoding = DEFAULT_ENCODING;

    @Inject
    public Saml11ValidationController(final CentralAuthenticationService centralAuthenticationService) {
        this.centralAuthenticationService = centralAuthenticationService;
    }

    @RequestMapping(method= RequestMethod.POST, value="/**/SAML/1.1/SOAP/Artifact")
    public void validateSaml11Artifact(final HttpServletRequest request, final Writer writer, @RequestBody final String body) throws IOException {
        final String soapActionHeader = request.getHeader(HEADER_SOAP_ACTION);

        if (!HEADER_SOAP_ACTION_VALUE.equals(soapActionHeader)) {
            throw new IllegalStateException("invalid soap action header.");
        }


        // strip off SOAP headers
        final String samlResponse = stripSOAPEnvelope(body);
        final TokenServiceAccessRequest tokenServiceAccessRequest = new Saml1TokenServiceAccessRequestImpl(samlResponse, request.getRemoteAddr());

        final ServiceAccessResponse serviceAccessResponse = this.centralAuthenticationService.validate(tokenServiceAccessRequest);

        writer.append("<?xml version=\"1.0\" encoding=\"");
        writer.append(this.encoding);
        writer.append("\"?>");
        writer.append("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\">\n");
        writer.append("   <SOAP-ENV:Header/>\n");
        writer.append("   <SOAP-ENV:Body>\n");
        final AccessResponseRequest accessResponseRequest = new DefaultAccessResponseRequestImpl(writer);
        serviceAccessResponse.generateResponse(accessResponseRequest);
        writer.append("   </SOAP-ENV:Body>\n");
        writer.append("</SOAP-ENV:Envelope>");
    }

    private String stripSOAPEnvelope(final String request) {

        final String withoutStart = request.substring(request.indexOf(SOAP_ENV_BODY)+SOAP_ENV_BODY.length());
        final String withoutEnd = withoutStart.substring(0, withoutStart.indexOf(SOAP_ENV_BODY_END.length()));
        return withoutEnd;
    }

    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }


    /**
 SOAPAction: http://www.oasis-open.org/committees/security
 <SOAP-ENV:Envelope
   xmlns:SOAP-ENV="http://schemas.xmlsoap.org/soap/envelope/">
   <SOAP-ENV:Header/>
   <SOAP-ENV:Body>
     <samlp:Request
       xmlns:samlp="urn:oasis:names:tc:SAML:1.0:protocol"
       MajorVersion="1" MinorVersion="1"
       RequestID="_192.168.16.51.1024506224022"
       IssueInstant="2002-06-19T17:03:44.022Z">
       <samlp:AssertionArtifact>
         artifact
       </samlp:AssertionArtifact>
     </samlp:Request>
   </SOAP-ENV:Body>
 </SOAP-ENV:Envelope>
     */
}
