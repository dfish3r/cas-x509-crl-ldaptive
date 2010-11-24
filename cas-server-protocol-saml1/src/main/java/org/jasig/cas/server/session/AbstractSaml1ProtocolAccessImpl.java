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

import org.jasig.cas.server.login.Saml1TokenServiceAccessRequestImpl;
import org.jasig.cas.server.login.TokenServiceAccessRequest;
import org.jasig.cas.server.util.ServiceIdentifierMatcher;
import org.opensaml.SAMLException;
import org.opensaml.SAMLResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.io.IOException;
import java.io.Writer;
import java.util.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSaml1ProtocolAccessImpl implements Access {

    private static final Logger logger = LoggerFactory.getLogger(AbstractSaml1ProtocolAccessImpl.class);

    /** Constant representing service. */
    private static final String CONST_PARAM_SERVICE = "TARGET";

    /** Constant representing artifact. */
    private static final String CONST_PARAM_TICKET = "SAMLart";

    protected enum ValidationStatus {NOT_VALIDATED, SUCCEEDED, USED, ALREADY_VALIDATED, VALIDATION_FAILED_ID_MATCH};

    protected abstract ValidationStatus getValidationStatus();

    protected abstract ServiceIdentifierMatcher getServiceIdentifierMatcher();

    protected abstract void setValidationStatus(ValidationStatus validationStatus);

    protected abstract String getEncoding();

    protected abstract String getBadMatchUrl();

    protected abstract void setBadMatchUrl(String badMatchUrl);

    public final boolean requiresStorage() {
        return true;
    }

    public final boolean isLocalSessionDestroyed() {
        return false;
    }

    public final synchronized void validate(final TokenServiceAccessRequest tokenServiceAccessRequest) {
        final ValidationStatus validationStatus = getValidationStatus();
        Assert.isInstanceOf(Saml1TokenServiceAccessRequestImpl.class, tokenServiceAccessRequest, "Invalid token validation request");

        final Saml1TokenServiceAccessRequestImpl saml1TokenServiceAccessRequest = (Saml1TokenServiceAccessRequestImpl) tokenServiceAccessRequest;

        if (validationStatus != ValidationStatus.NOT_VALIDATED) {
            setValidationStatus(ValidationStatus.ALREADY_VALIDATED);
            return;
        }

        if (!getServiceIdentifierMatcher().matches(getResourceIdentifier(), tokenServiceAccessRequest.getServiceId())) {
            setValidationStatus(ValidationStatus.VALIDATION_FAILED_ID_MATCH);
            return;
        }

        setValidationStatus(ValidationStatus.SUCCEEDED);
    }

    public final boolean isUsed() {
        return getValidationStatus() != ValidationStatus.NOT_VALIDATED;
    }

    public AccessResponseResult generateResponse(final AccessResponseRequest accessResponseRequest) {
        switch (getValidationStatus()) {

            case NOT_VALIDATED:
                final Map<String, List<String>> parameters = new HashMap<String, List<String>>();
                parameters.put(CONST_PARAM_TICKET, Arrays.asList(getId()));
                parameters.put(CONST_PARAM_SERVICE, Arrays.asList(getResourceIdentifier()));

                return new DefaultAccessResponseResultImpl(AccessResponseResult.Operation.REDIRECT, parameters, getResourceIdentifier(), null, null);

            case USED:
                return constructErrorResponse(accessResponseRequest.getWriter(), "This artifact id has already been used.");

            case VALIDATION_FAILED_ID_MATCH:
                return constructErrorResponse(accessResponseRequest.getWriter(), String.format("Original url of %s does not match supplied url for validation of %s", getResourceIdentifier(), getBadMatchUrl()));

            default: // succeeded



        }

        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected final AccessResponseResult constructErrorResponse(final Writer writer, final String errorMessage) {
        try {
            final SAMLResponse samlResponse = new SAMLResponse(getId(), getResourceIdentifier(), new ArrayList<Object>(), new SAMLException(errorMessage));
            samlResponse.setIssueInstant(new Date());

            writer.write("<?xml version=\"1.0\" encoding=\"" + getEncoding() + "\"?>");
            writer.write("<SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Header/><SOAP-ENV:Body>");
            writer.write(samlResponse.toString());
            writer.write("</SOAP-ENV:Body></SOAP-ENV:Envelope>");
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
        }

        return new DefaultAccessResponseResultImpl("text/xml; charset=" + getEncoding());
    }
}
