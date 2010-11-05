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

import org.jasig.cas.server.CasProtocolVersion;
import org.jasig.cas.server.login.CasTokenServiceAccessRequestImpl;
import org.jasig.cas.server.login.TokenServiceAccessRequest;
import org.jasig.cas.server.util.ServiceIdentifierMatcher;
import org.jasig.cas.server.util.UniqueTicketIdGenerator;
import org.jasig.cas.util.HttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Assert;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Implements the CAS protocol, but leaves the data storage to the specific implementations (i.e. In Memory, Database, etc.)
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractCasProtocolAccessImpl implements Access {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractCasProtocolAccessImpl.class);

    protected enum ValidationStatus {NOT_VALIDATED, VALIDATION_SUCCEEDED, VALIDATION_FAILED_RENEW, VALIDATION_FAILED_ID_MATCH, VALIDATION_FAILED_PROXY_ATTEMPT, ALREADY_VALIDATED, EXPIRED_TICKET};

    protected static final Map<ValidationStatus,String> VALIDATION_STATUS_TO_ERROR_CODE_MAPPING = new HashMap<ValidationStatus,String>();

    static {
        VALIDATION_STATUS_TO_ERROR_CODE_MAPPING.put(ValidationStatus.VALIDATION_FAILED_RENEW, "INVALID_TICKET");
        VALIDATION_STATUS_TO_ERROR_CODE_MAPPING.put(ValidationStatus.VALIDATION_FAILED_ID_MATCH, "INVALID_SERVICE");
        VALIDATION_STATUS_TO_ERROR_CODE_MAPPING.put(ValidationStatus.VALIDATION_FAILED_PROXY_ATTEMPT, "INVALID_TICKET");
        VALIDATION_STATUS_TO_ERROR_CODE_MAPPING.put(ValidationStatus.ALREADY_VALIDATED, "INVALID_TICKET");
        VALIDATION_STATUS_TO_ERROR_CODE_MAPPING.put(ValidationStatus.EXPIRED_TICKET, "INVALID_TICKET");
    }

    public final synchronized void validate(final TokenServiceAccessRequest tokenServiceAccessRequest) {
        final ValidationStatus validationStatus = getValidationStatus();
        Assert.isInstanceOf(CasTokenServiceAccessRequestImpl.class, tokenServiceAccessRequest, "Invalid token validation request");

        final CasTokenServiceAccessRequestImpl casTokenServiceAccessRequest = (CasTokenServiceAccessRequestImpl) tokenServiceAccessRequest;
        setCasProtocolVersion(casTokenServiceAccessRequest.getCasVersion());

        if (validationStatus != ValidationStatus.NOT_VALIDATED) {
            setValidationStatus(ValidationStatus.ALREADY_VALIDATED);
            return;
        }

        if (tokenServiceAccessRequest.isForceAuthentication() && !isRenewed()) {
            setValidationStatus(ValidationStatus.VALIDATION_FAILED_RENEW);
            return;
        }

        if (getCasVersion() != CasProtocolVersion.CAS2_WITH_PROXYING && !getParentSession().getProxiedAuthentications().isEmpty()) {
            setValidationStatus(ValidationStatus.VALIDATION_FAILED_PROXY_ATTEMPT);
            return;
        }

        if (!getServiceIdentifierMatcher().matches(getResourceIdentifier(), tokenServiceAccessRequest.getServiceId())) {
            setValidationStatus(ValidationStatus.VALIDATION_FAILED_ID_MATCH);
            return;
        }

        setValidationStatus(ValidationStatus.VALIDATION_SUCCEEDED);
    }

    public final boolean isUsed() {
        return getValidationStatus() != ValidationStatus.NOT_VALIDATED;
    }

    public final boolean invalidate() {
        if (getValidationStatus() != ValidationStatus.VALIDATION_SUCCEEDED) {
            return false;
        }

        setValidationStatus(ValidationStatus.EXPIRED_TICKET);

        LOG.debug("Sending logout request for: " + getId());

        final String logoutRequest = "<samlp:LogoutRequest xmlns:samlp=\"urn:oasis:names:tc:SAML:2.0:protocol\" ID=\""
            + getIdGenerator().getNewTicketId("LR")
            + "\" Version=\"2.0\" IssueInstant=\"" + getCurrentDateAndTime()
            + "\"><saml:NameID xmlns:saml=\"urn:oasis:names:tc:SAML:2.0:assertion\">@NOT_USED@</saml:NameID><samlp:SessionIndex>"
            + getId() + "</samlp:SessionIndex></samlp:LogoutRequest>";

        setLocalSessionDestroyed(new HttpClient().sendMessageToEndPoint(getResourceIdentifier(), logoutRequest, true));

        return this.isLocalSessionDestroyed();
    }

    protected final String getCurrentDateAndTime() {
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        return dateFormat.format(new Date());
    }

    public final boolean requiresStorage() {
        return true;
    }

    protected final boolean isExpired() {
        return getExpirationPolicy().isExpired(getState()) || getValidationStatus() == ValidationStatus.EXPIRED_TICKET; 
    }

    public final synchronized AccessResponseResult generateResponse(final AccessResponseRequest accessResponseRequest) {

        if (getValidationStatus() == ValidationStatus.NOT_VALIDATED) {
            return generateNotValidatedResponse(accessResponseRequest);
        }

        final String casVersionPrefix = getCasVersion().asString();

        if (isExpired()) {
            setValidationStatus(ValidationStatus.EXPIRED_TICKET);
        }

        getState().updateState();

        final Map<String, Object> parameters = new HashMap<String, Object>();

        switch (getValidationStatus()) {
            case VALIDATION_SUCCEEDED:
                final String successFileName = casVersionPrefix + "successResponseTemplate.ftl";
                parameters.put("session", getParentSession());

                if (accessResponseRequest.getProxySessionId() != null) {
                    final String pgtIou = getProxyHandler().handleProxyGrantingRequest(accessResponseRequest.getProxySessionId(), accessResponseRequest.getProxiedCredential());

                    if (pgtIou != null) {
                        parameters.put("pgtIou", pgtIou);
                    }
                }

//                FreemarkerUtils.writeToFreeMarkerTemplate(successFileName, parameters, accessResponseRequest.getWriter());
                return DefaultAccessResponseResultImpl.NONE;

            default:
                final String errorFileName = casVersionPrefix + "errorResponseTemplate.ftl";
                parameters.put("errorCode", VALIDATION_STATUS_TO_ERROR_CODE_MAPPING.get(getValidationStatus()));
                if (getValidationStatus() == ValidationStatus.VALIDATION_FAILED_ID_MATCH) {
//                    parameters.put("message", getMessageSourceAccessor().getMessage(getValidationStatus().name(), new Object[] {getResourceIdentifier()}));
                } else {
//                    parameters.put("message", getMessageSourceAccessor().getMessage(getValidationStatus().name()));
                }
                parameters.put("message", "Get MESSAGE From accessor");
//                FreemarkerUtils.writeToFreeMarkerTemplate(errorFileName, parameters, accessResponseRequest.getWriter());

                return DefaultAccessResponseResultImpl.NONE;
        }
    }

    protected AccessResponseResult generateNotValidatedResponse(final AccessResponseRequest accessResponseRequest) {
        if (getParentSession() != null && getParentSession().getProxiedAuthentications().isEmpty()) {
            final Map<String, List<String>> parameters = new HashMap<String,  List<String>>();
            parameters.put("ticket", Arrays.asList(getId()));

            final AccessResponseResult.Operation operation = this.isPostRequest() ? AccessResponseResult.Operation.POST : AccessResponseResult.Operation.REDIRECT;
            return new DefaultAccessResponseResultImpl(operation, parameters, getResourceIdentifier(), null, null);
        } else {
            final Map<String, Object> root = new HashMap<String, Object>();
            root.put("ticket", getId());
//            FreemarkerUtils.writeToFreeMarkerTemplate("CAS2successResponseProxyTemplate.ftl", root, accessResponseRequest.getWriter());
            return DefaultAccessResponseResultImpl.NONE;
        }
    }

    /**
     * Creates the unique identifiers for the ticket.  Reads the system property to obtain a unique suffix if required.
     *
     * @return the unique identifier for this ticket.
     */
    protected String createId() {
        if (getParentSession().isRoot()) {
            return getIdGenerator().getNewTicketId("ST");
        } else {
            return getIdGenerator().getNewTicketId("PT");
        }
    }

    /**
     * Retrieves the current validation status of the Access object.
     *
     * @return the current ValidationStatus.  CANNOT be null.
     */
    protected abstract ValidationStatus getValidationStatus();

    /**
     * Updates the ValidationStatus to the new status.
     *
     * @param validationStatus the new ValidationStatus.  CANNOT be null.
     */
    protected abstract void setValidationStatus(ValidationStatus validationStatus);

    /**
     * Retrieves the internal state of this Access.
     *
     * @return the internal state. CANNOT be null.
     */
    protected abstract State getState();

    /**
     * Returns whether a service was generated from a new Authentication request or not.
     *
     * @return true if it was, false otherwise.
     */
    protected abstract boolean isRenewed();

    /**
     * Returns the implementation of the {@link org.jasig.cas.server.util.ServiceIdentifierMatcher} that can match services.
     * <p>
     * CANNOT be null.
     * @return the service identifier matcher.
     */
    protected abstract ServiceIdentifierMatcher getServiceIdentifierMatcher();

    /**
     * Was the original request a request for a POST response.
     *
     * @return true if it was, false otherwise;
     */
    protected abstract boolean isPostRequest();

    /**
     * Returns the CAS version, or null if one has not been set yet.
     * @return the CAS version.
     */
    protected abstract CasProtocolVersion getCasVersion();

    /**
     * Sets the CAS version.
     * @param casVersion the CAS version.
     */
    protected abstract void setCasProtocolVersion(CasProtocolVersion casVersion);

    protected abstract ProxyHandler getProxyHandler();

    protected abstract Session getParentSession();

    protected abstract ExpirationPolicy getExpirationPolicy();

    /**
     * Returns the UniqueTicketId Generator to use to create ids.  CANNOT be NULL.
     * @return the id generator
     */
    protected abstract UniqueTicketIdGenerator getIdGenerator();

    protected abstract void setLocalSessionDestroyed(boolean localSessionDestroyed);
}
