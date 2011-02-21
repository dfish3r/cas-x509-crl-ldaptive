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

import org.jasig.cas.server.Saml11Profile;
import org.jasig.cas.server.authentication.Authentication;
import org.jasig.cas.server.login.Saml1TokenServiceAccessRequestImpl;
import org.jasig.cas.server.login.TokenServiceAccessRequest;
import org.jasig.cas.server.util.ServiceIdentifierMatcher;
import org.jasig.cas.server.util.XmlMarshallingUtils;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.saml1.core.*;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.opensaml.xml.io.Marshaller;
import org.opensaml.xml.io.MarshallerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;
import org.w3c.dom.Element;

import javax.xml.namespace.QName;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public abstract class AbstractSaml1ProtocolAccessImpl implements Access {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSaml1ProtocolAccessImpl.class);

    private static final String NAMESPACE = "http://www.ja-sig.org/products/cas/";

    /** Constant representing service. */
    private static final String CONST_PARAM_SERVICE = "TARGET";

    /** Constant representing artifact. */
    private static final String CONST_PARAM_TICKET = "SAMLart";

    private static final String CONST_PARAM_RESPONSE = "SAMLResponse";

    protected enum ValidationStatus {NOT_VALIDATED, SUCCEEDED, USED, ALREADY_VALIDATED, VALIDATION_FAILED_ID_MATCH}

    protected abstract ValidationStatus getValidationStatus();

    protected abstract ServiceIdentifierMatcher getServiceIdentifierMatcher();

    protected abstract void setValidationStatus(ValidationStatus validationStatus);

    protected abstract String getBadMatchUrl();

    protected abstract Saml11Profile getProfile();

    protected abstract void setBadMatchUrl(String badMatchUrl);

    protected abstract Session getParentSession();

    protected abstract String getIssuer();

    protected abstract long getIssueLength();

    protected abstract void setRequestId(String requestId);

    protected abstract String getRequestId();

    public final boolean requiresStorage() {
        return Saml11Profile.BrowserArtifact.equals(getProfile());
    }

    public final boolean isLocalSessionDestroyed() {
        return false;
    }

    public final synchronized void validate(final TokenServiceAccessRequest tokenServiceAccessRequest) {
        if (Saml11Profile.BrowserPost.equals(getProfile())) {
            throw new IllegalStateException("You should not be attempting to validate this type of profile.");
        }

        final ValidationStatus validationStatus = getValidationStatus();
        Assert.isInstanceOf(Saml1TokenServiceAccessRequestImpl.class, tokenServiceAccessRequest, "Invalid token validation request");

        final Saml1TokenServiceAccessRequestImpl saml1TokenServiceAccessRequest = (Saml1TokenServiceAccessRequestImpl) tokenServiceAccessRequest;
        setRequestId(saml1TokenServiceAccessRequest.getRequestId());

        if (validationStatus != ValidationStatus.NOT_VALIDATED) {
            setValidationStatus(ValidationStatus.ALREADY_VALIDATED);
            return;
        }

        if (!getServiceIdentifierMatcher().matches(getResourceIdentifier(), tokenServiceAccessRequest.getServiceId())) {
            setValidationStatus(ValidationStatus.VALIDATION_FAILED_ID_MATCH);
            setBadMatchUrl(tokenServiceAccessRequest.getServiceId());
            return;
        }

        setValidationStatus(ValidationStatus.SUCCEEDED);
    }

    public final boolean isUsed() {
        return getValidationStatus() != ValidationStatus.NOT_VALIDATED;
    }

    public boolean invalidate() {
       return false;
    }

    public AccessResponseResult generateResponse(final AccessResponseRequest accessResponseRequest) {
        if (Saml11Profile.BrowserPost.equals(getProfile())) {
            final Map<String, List<String>> values = new HashMap<String, List<String>>();
            values.put(CONST_PARAM_SERVICE, Arrays.asList(getResourceIdentifier()));
            values.put(CONST_PARAM_RESPONSE, Arrays.asList(XmlMarshallingUtils.marshall(generateSuccessResponse())));

            return DefaultAccessResponseResultImpl.generatePostRedirect(getResourceIdentifier(), values);
        }

        final Writer writer = accessResponseRequest.getWriter();
        switch (getValidationStatus()) {

            case NOT_VALIDATED:
                final Map<String, List<String>> parameters = new HashMap<String, List<String>>();
                parameters.put(CONST_PARAM_TICKET, Arrays.asList(getId()));
                parameters.put(CONST_PARAM_SERVICE, Arrays.asList(getResourceIdentifier()));

                return DefaultAccessResponseResultImpl.generateRedirect(getResourceIdentifier(), parameters);

            case USED:
                return constructErrorResponse(writer, StatusCode.TOO_MANY_RESPONSES, "This artifact id has already been used.");

            case VALIDATION_FAILED_ID_MATCH:
                return constructErrorResponse(writer, StatusCode.RESOURCE_NOT_RECOGNIZED, String.format("Original url of %s does not match supplied url for validation of %s", getResourceIdentifier(), getBadMatchUrl()));

            default: // succeeded
                final String successResponse = XmlMarshallingUtils.marshall(generateSuccessResponse());
                try {
                    writer.write(successResponse);
                } catch (final IOException e) {
                    logger.error(e.getMessage(), e);
                }
        }

        setValidationStatus(ValidationStatus.ALREADY_VALIDATED);
        return DefaultAccessResponseResultImpl.NONE;
    }

    protected final AccessResponseResult constructErrorResponse(final Writer writer, final QName statusCodeEnum, final String errorMessage) {
        try {
            final XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
            final SAMLObjectBuilder<Response> responseBuilder = (SAMLObjectBuilder<Response>) builderFactory.getBuilder(Response.DEFAULT_ELEMENT_NAME);
            final SAMLObjectBuilder<Status> statusBuilder = (SAMLObjectBuilder<Status>) builderFactory.getBuilder(Status.DEFAULT_ELEMENT_NAME);
            final SAMLObjectBuilder<StatusCode> statusCodeBuilder = (SAMLObjectBuilder<StatusCode>) builderFactory.getBuilder(StatusCode.DEFAULT_ELEMENT_NAME);
            final SAMLObjectBuilder<StatusDetail> statusDetailBuilder = (SAMLObjectBuilder<StatusDetail>) builderFactory.getBuilder(StatusDetail.DEFAULT_ELEMENT_NAME);
            final SAMLObjectBuilder<StatusMessage> statusMessageBuilder = (SAMLObjectBuilder<StatusMessage>) builderFactory.getBuilder(StatusMessage.DEFAULT_ELEMENT_NAME);

            final Response response = responseBuilder.buildObject();
            final Status status = statusBuilder.buildObject();
            final StatusCode statusCode = statusCodeBuilder.buildObject();
            final StatusDetail statusDetail = statusDetailBuilder.buildObject();
            final StatusMessage statusMessage = statusMessageBuilder.buildObject();

            statusCode.setValue(statusCodeEnum);

            statusMessage.setMessage(errorMessage);

            status.setStatusCode(statusCode);
            status.setStatusDetail(statusDetail);
            status.setStatusMessage(statusMessage);
            response.setInResponseTo(getRequestId());

            response.setStatus(status);

            writer.write(XmlMarshallingUtils.marshall(response));
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
        }

        // TODO removed the encoding from here
        return DefaultAccessResponseResultImpl.none("text/xml; charset=");
    }

    private Response generateSuccessResponse() {
        final XMLObjectBuilderFactory builderFactory = Configuration.getBuilderFactory();
        final SAMLObjectBuilder<Response> responseBuilder = (SAMLObjectBuilder<Response>) builderFactory.getBuilder(Response.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<Status> statusBuilder = (SAMLObjectBuilder<Status>) builderFactory.getBuilder(Status.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<StatusCode> statusCodeBuilder = (SAMLObjectBuilder<StatusCode>) builderFactory.getBuilder(StatusCode.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<StatusDetail> statusDetailBuilder = (SAMLObjectBuilder<StatusDetail>) builderFactory.getBuilder(StatusDetail.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<StatusMessage> statusMessageBuilder = (SAMLObjectBuilder<StatusMessage>) builderFactory.getBuilder(StatusMessage.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<Assertion> assertionBuilder = (SAMLObjectBuilder<Assertion>) builderFactory.getBuilder(Assertion.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<AudienceRestrictionCondition> audienceRestrictionConditionSAMLObjectBuilder = (SAMLObjectBuilder<AudienceRestrictionCondition>) builderFactory.getBuilder(AudienceRestrictionCondition.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<Audience> audienceSAMLObjectBuilder = (SAMLObjectBuilder<Audience>) builderFactory.getBuilder(Audience.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<AuthenticationStatement> authenticationStatementSAMLObjectBuilder = (SAMLObjectBuilder<AuthenticationStatement>) builderFactory.getBuilder(AuthenticationStatement.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<Subject> subjectSAMLObjectBuilder = (SAMLObjectBuilder<Subject>) builderFactory.getBuilder(Audience.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<NameIdentifier> nameIdentifierSAMLObjectBuilder = (SAMLObjectBuilder<NameIdentifier>) builderFactory.getBuilder(NameIdentifier.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<AttributeStatement> attributeStatementSAMLObjectBuilder = (SAMLObjectBuilder<AttributeStatement>) builderFactory.getBuilder(AttributeStatement.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<Attribute> attributeSAMLObjectBuilder = (SAMLObjectBuilder<Attribute>) builderFactory.getBuilder(Attribute.DEFAULT_ELEMENT_NAME);
        final SAMLObjectBuilder<AttributeValue> attributeValueSAMLObjectBuilder = (SAMLObjectBuilder<AttributeValue>) builderFactory.getBuilder(AttributeValue.DEFAULT_ELEMENT_NAME);

        final Response response = responseBuilder.buildObject();
        response.setInResponseTo(getRequestId());
        response.setID(UUID.randomUUID().toString());
        response.setIssueInstant(new DateTime());
        response.setRecipient(getResourceIdentifier());

        final Status status = statusBuilder.buildObject();
        final StatusCode statusCode = statusCodeBuilder.buildObject();
        final StatusDetail statusDetail = statusDetailBuilder.buildObject();
        final StatusMessage statusMessage = statusMessageBuilder.buildObject();

        statusCode.setValue(StatusCode.SUCCESS);
        statusMessage.setMessage("Success");

        status.setStatusCode(statusCode);
        status.setStatusDetail(statusDetail);
        status.setStatusMessage(statusMessage);

        response.setStatus(status);

        final Assertion assertion = assertionBuilder.buildObject();

        assertion.setIssueInstant(new DateTime(DateTimeZone.UTC));
        assertion.setIssuer(getIssuer());
        final AudienceRestrictionCondition audienceRestrictionCondition = audienceRestrictionConditionSAMLObjectBuilder.buildObject();
        final Audience audience = audienceSAMLObjectBuilder.buildObject();
        audience.setUri(getResourceIdentifier());
        audienceRestrictionCondition.getAudiences().add(audience);

        assertion.getConditions().getAudienceRestrictionConditions().add(audienceRestrictionCondition);
        assertion.getConditions().setNotBefore(new DateTime(DateTimeZone.UTC));
        assertion.getConditions().setNotOnOrAfter(new DateTime(System.currentTimeMillis() + getIssueLength(), DateTimeZone.UTC));

        final Subject subject = subjectSAMLObjectBuilder.buildObject();
        final NameIdentifier nameIdentifier = nameIdentifierSAMLObjectBuilder.buildObject();
        nameIdentifier.setNameIdentifier(getParentSession().getRootPrincipal().getName());
        subject.setNameIdentifier(nameIdentifier);

        for (final Authentication authentication : getParentSession().getRootAuthentications()) {
            final AuthenticationStatement authenticationStatement = authenticationStatementSAMLObjectBuilder.buildObject();
            authenticationStatement.setAuthenticationInstant(new DateTime(authentication.getAuthenticationDate().getTime(), DateTimeZone.UTC));
            authenticationStatement.setAuthenticationMethod(authentication.getAuthenticationMethod());
            authenticationStatement.setSubject(subject);
            assertion.getAuthenticationStatements().add(authenticationStatement);
        }

        if (!getParentSession().getRootPrincipal().getAttributes().isEmpty()) {
            final AttributeStatement attributeStatement = attributeStatementSAMLObjectBuilder.buildObject();
            attributeStatement.setSubject(subject);

            for (final Map.Entry<String, List<Object>> entry : getParentSession().getRootPrincipal().getAttributes().entrySet()) {
                if (!entry.getValue().isEmpty()) {
                    final Attribute attribute = attributeSAMLObjectBuilder.buildObject();
                    attribute.setAttributeNamespace(NAMESPACE);
                    attribute.setAttributeName(entry.getKey());

                    for (final Object o : entry.getValue()) {
                        final AttributeValue av = attributeValueSAMLObjectBuilder.buildObject();
                        attribute.getAttributeValues().add(av);
                    }
                    attributeStatement.getAttributes().add(attribute);
                }
            }
        }

        response.getAssertions().add(assertion);
        return response;
    }
}
