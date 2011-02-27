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

package org.jasig.cas.server.login;

import org.apache.commons.lang.StringUtils;
import org.jasig.cas.server.session.Access;
import org.jasig.cas.server.session.AccessResponseRequest;
import org.jasig.cas.server.session.AccessResponseResult;
import org.jasig.cas.server.session.DefaultAccessResponseResultImpl;
import org.jasig.cas.server.util.XmlMarshallingUtils;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObjectBuilder;
import org.opensaml.saml1.core.*;
import org.opensaml.xml.XMLObjectBuilderFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

/**
 * Response to a request for a SAML1.1 token request.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class Saml11TokenServiceAccessResponseImpl extends DefaultLoginResponseImpl implements ServiceAccessResponse {

    private static final Logger logger = LoggerFactory.getLogger(Saml11TokenServiceAccessResponseImpl.class);

    private final Access access;

    private final Saml11TokenServiceAccessRequestImpl serviceAccessRequest;

    public Saml11TokenServiceAccessResponseImpl(final Access access) {
        super(null, null);
        this.access = access;
        this.serviceAccessRequest = null;
    }

    public Saml11TokenServiceAccessResponseImpl(final Saml11TokenServiceAccessRequestImpl serviceAccessRequest) {
        super(null, null);
        this.serviceAccessRequest = serviceAccessRequest;
        this.access = null;
    }

    public AccessResponseResult generateResponse(final AccessResponseRequest accessResponseRequest) {
        if (this.access != null) {
            return this.access.generateResponse(accessResponseRequest);
        }

        // otherwise just return a request error
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

            if (StringUtils.isBlank(this.serviceAccessRequest.getRequestId())) {
                statusCode.setValue(StatusCode.REQUEST_DENIED);
                statusMessage.setMessage("Token not provided.");
            } else {
                statusCode.setValue(StatusCode.RESOURCE_NOT_RECOGNIZED);
                statusMessage.setMessage("Token not recognized.");
            }

            status.setStatusCode(statusCode);
            status.setStatusDetail(statusDetail);
            status.setStatusMessage(statusMessage);
            response.setInResponseTo(this.serviceAccessRequest.getRequestId());

            response.setStatus(status);

            accessResponseRequest.getWriter().write(XmlMarshallingUtils.marshall(response));
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
        }

        // TODO removed the encoding from here
        return DefaultAccessResponseResultImpl.none("text/xml; charset=");
    }

    public List<Access> getLoggedOutAccesses() {
        return Collections.emptyList();
    }
}
