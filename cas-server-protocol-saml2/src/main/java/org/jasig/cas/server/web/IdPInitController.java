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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.text.StrSubstitutor;
import org.jasig.cas.server.util.SamlCompliantThreadLocalDateFormatDateParser;
import org.jasig.cas.server.util.SamlUtils;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Initiate a single sign on request from the IdP side.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@Controller
public class IdPInitController {

    private static final String AUTHENTICATION_REQUEST_TEMPLATE = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
            "<samlp:AuthnRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" ID=\"${generatedId}\" Version=\"2.0\" IssueInstant=\"${issueInstant}\" ProtocolBinding=\"urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST\" ProviderName=\"${providerName}\" IsPassive=\"false\" AssertionConsumerServiceURL=\"${assertionConsumerUrl}\">\n" +
            "  <saml:Issuer xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">https://www.salesforce.com</saml:Issuer>\n" +
            "  <samlp:NameIDPolicy AllowCreate=\"true\" Format=\"urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified\" />\n" +
            "</samlp:AuthnRequest>";

    @NotNull
    private final Map<String, String> issuerToAssertionConsumerUrlMapping;

    @NotNull
    private final Map<String, String> issuerToProviderNameMapping;

    @NotNull
    private final SamlCompliantThreadLocalDateFormatDateParser dateParser;

    public IdPInitController(final Map<String, String> issuerToAssertionConsumerUrlMapping, final Map<String, String> issuerToProviderNameMapping, final SamlCompliantThreadLocalDateFormatDateParser dateParser) {
        this.issuerToAssertionConsumerUrlMapping = issuerToAssertionConsumerUrlMapping;
        this.issuerToProviderNameMapping = issuerToProviderNameMapping;
        this.dateParser = dateParser;
    }

    @RequestMapping(method = RequestMethod.GET, value = "/idp/saml2/request/issuer/{issuer}")
    public void generateAuthenticationRequest(final HttpServletRequest request, final HttpServletResponse response, @RequestParam(required = true, value = "issuer") final String issuer) throws IOException {
        final String assertionConsumerUrl = this.issuerToAssertionConsumerUrlMapping.get(issuer);
        final String providerName = this.issuerToProviderNameMapping.get(issuer);

        Assert.notNull(assertionConsumerUrl,String.format("No mapping exists for [%s] in assertion consumer url"));
        Assert.notNull(providerName, String.format("No mapping exists for [%s] in provider name", issuer));

        final Map<String, String> subs = new HashMap<String, String>();

        subs.put("generatedId", UUID.randomUUID().toString());
        subs.put("issueInstant", this.dateParser.format(new Date()));
        subs.put("providerName", providerName);
        subs.put("assertionConsumerUrl", assertionConsumerUrl);

        final StrSubstitutor strSubstitutor = new StrSubstitutor(subs);
        final String samlRequest = strSubstitutor.replace(AUTHENTICATION_REQUEST_TEMPLATE);
        final String based64version = Base64.encodeBase64String(samlRequest.getBytes());

        final String redirectUrl = "/" + request.getContextPath() + "/login?SAMLRequest=" + based64version;

        response.sendRedirect(redirectUrl);
    }  
}
