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

package org.jasig.cas.web.view;

import java.util.*;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.constraints.NotNull;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.validation.Assertion;
import org.opensaml.SAMLAssertion;
import org.opensaml.SAMLAttribute;
import org.opensaml.SAMLAttributeStatement;
import org.opensaml.SAMLAudienceRestrictionCondition;
import org.opensaml.SAMLAuthenticationStatement;
import org.opensaml.SAMLException;
import org.opensaml.SAMLNameIdentifier;
import org.opensaml.SAMLResponse;
import org.opensaml.SAMLSubject;

/**
 * Implementation of a view to return a SAML response and assertion, based on
 * the SAML 1.1 specification.
 * <p>
 * If an AttributePrincipal is supplied, then the assertion will include the
 * attributes from it (assuming a String key/Object value pair). The only
 * Authentication attribute it will look at is the authMethod (if supplied).
 * <p>
 * Note that this class will currently not handle proxy authentication.
 * <p>
 * Note: This class currently expects a bean called "ServiceRegistry" to exist.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.1
 */
public class Saml10SuccessResponseView extends AbstractCasView {

    /** Namespace for custom attributes. */
    private static final String NAMESPACE = "http://www.ja-sig.org/products/cas/";

    private static final String DEFAULT_ENCODING = "UTF-8";

    /** The issuer, generally the hostname. */
    @NotNull
    private String issuer;

    /** The amount of time in milliseconds this is valid for. */
    private long issueLength = 30000;

    @NotNull
    private String encoding = DEFAULT_ENCODING;

    @Override
    protected void renderMergedOutputModel(final Map model,
        final HttpServletRequest request, final HttpServletResponse response)
        throws Exception {

        try {
            final Assertion assertion = getAssertionFrom(model);
            final Authentication authentication = assertion
                .getChainedAuthentications().get(0);
            final Date currentDate = new Date();
            final String authenticationMethod = authentication.getAuthenticationMethod();
            final Service service = assertion.getService();
            final SAMLResponse samlResponse = new SAMLResponse(null, service
                .getId(), new ArrayList<Object>(), null);

            samlResponse.setIssueInstant(currentDate);

            final SAMLAssertion samlAssertion = new SAMLAssertion();
            samlAssertion.setIssueInstant(currentDate);
            samlAssertion.setIssuer(this.issuer);
            samlAssertion.setNotBefore(currentDate);
            samlAssertion.setNotOnOrAfter(new Date(currentDate.getTime()
                + this.issueLength));

            final SAMLAudienceRestrictionCondition samlAudienceRestrictionCondition = new SAMLAudienceRestrictionCondition();
            samlAudienceRestrictionCondition.addAudience(service.getId());

            final SAMLAuthenticationStatement samlAuthenticationStatement = new SAMLAuthenticationStatement();
            samlAuthenticationStatement.setAuthInstant(authentication.getAuthenticationDate());
            samlAuthenticationStatement
                .setAuthMethod(authenticationMethod != null
                    ? authenticationMethod
                    : SAMLAuthenticationStatement.AuthenticationMethod_Unspecified);

            samlAuthenticationStatement
                .setSubject(getSamlSubject(assertion));

            if (!assertion.getPrincipal().getAttributes().isEmpty()) {
                final SAMLAttributeStatement attributeStatement = new SAMLAttributeStatement();
    
                attributeStatement.setSubject(getSamlSubject(assertion));
                samlAssertion.addStatement(attributeStatement);

                for (final Entry<String, List<Object>> e : assertion.getPrincipal().getAttributes().entrySet()) {
                    final SAMLAttribute attribute = new SAMLAttribute();
                    attribute.setName(e.getKey());
                    attribute.setNamespace(NAMESPACE);

                    if (e.getValue() instanceof Collection<?>) {
                        final Collection<?> c = (Collection<?>) e.getValue();
                        if (c.isEmpty()) {
                            // 100323 bnoordhuis: don't add the attribute, it causes a org.opensaml.MalformedException
                            continue;
                        }
                        attribute.setValues(c);
                    } else {
                        attribute.addValue(e.getValue());
                    }
    
                    attributeStatement.addAttribute(attribute);
                }
            }

            samlAssertion.addStatement(samlAuthenticationStatement);
            samlAssertion.addCondition(samlAudienceRestrictionCondition);
            samlResponse.addAssertion(samlAssertion);

            final String xmlResponse = samlResponse.toString();

            response.setContentType("text/xml; charset=" + this.encoding);
            response.getWriter().print("<?xml version=\"1.0\" encoding=\"" + this.encoding + "\"?>");
            response.getWriter().print("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body>");
            response.getWriter().print(xmlResponse);
            response.getWriter().print("</SOAP-ENV:Body></SOAP-ENV:Envelope>");
            response.flushBuffer();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            throw e;
        }
    }

    protected SAMLSubject getSamlSubject(final Assertion assertion)
        throws SAMLException {
        final SAMLSubject samlSubject = new SAMLSubject();
        samlSubject.addConfirmationMethod(SAMLSubject.CONF_ARTIFACT);
        final SAMLNameIdentifier samlNameIdentifier = new SAMLNameIdentifier();
        samlNameIdentifier.setName(assertion.getPrincipal().getName());

        samlSubject.setNameIdentifier(samlNameIdentifier);

        return samlSubject;
    }

    public void setIssueLength(final long issueLength) {
        this.issueLength = issueLength;
    }

    public void setIssuer(final String issuer) {
        this.issuer = issuer;
    }

    public void setEncoding(final String encoding) {
        this.encoding = encoding;
    }
}
