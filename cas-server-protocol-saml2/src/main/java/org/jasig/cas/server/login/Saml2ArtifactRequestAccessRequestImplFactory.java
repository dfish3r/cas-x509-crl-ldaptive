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

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang.StringUtils;
import org.jasig.cas.util.SamlUtils;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import java.util.zip.InflaterInputStream;

/**
 * Constructs a {@link org.jasig.cas.server.login.Saml2ArtifactRequestAccessRequestImpl}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class Saml2ArtifactRequestAccessRequestImplFactory extends AbstractServiceAccessRequestFactory {

    private static final String CONSTANT_PARAMETER_SERVICE = "SAMLRequest";

    private static final String CONSTANT_RELAY_STATE = "RelayState";

    private String alternateUserName;

    public ServiceAccessRequest getServiceAccessRequest(final String sessionId, final String remoteIpAddress, final Map parameters) {
        final String samlRequest = decodeAuthnRequestXML(getValue(parameters.get(CONSTANT_PARAMETER_SERVICE)));

        if (StringUtils.isEmpty(samlRequest)) {
            return null;
        }

        final Document document = SamlUtils.constructDocumentFromXmlString(samlRequest);

        if (document == null) {
            return null;
        }

        final String assertionConsumerServiceUrl = document.getRootElement().getAttributeValue("AssertionConsumerServiceURL");
        final String requestId = document.getRootElement().getAttributeValue("ID");
        final String relayState = getValue(parameters.get(CONSTANT_RELAY_STATE));

        return new Saml2ArtifactRequestAccessRequestImpl(sessionId, remoteIpAddress, false, false, null, assertionConsumerServiceUrl, requestId, this.alternateUserName, relayState);
    }

    /**
     * Sets an alternate username to send to the SAML2-compliant application (i.e. fully qualified email address).  Relies on an appropriate
     * attribute available for the user.
     * <p>
     * Note that this is optional and the default is to use the normal identifier.
     *
     * @param alternateUserName the alternate user name.  This is OPTIONAL.
     */
    public void setAlternateUserName(final String alternateUserName) {
        this.alternateUserName = alternateUserName;
    }

    private static String decodeAuthnRequestXML(
        final String encodedRequestXmlString) {
        if (encodedRequestXmlString == null) {
            return null;
        }

        final byte[] decodedBytes = base64Decode(encodedRequestXmlString);

        if (decodedBytes == null) {
            return null;
        }

        final String inflated = inflate(decodedBytes);

        if (inflated != null) {
            return inflated;
        }

        return zlibDeflate(decodedBytes);
    }

    private static String zlibDeflate(final byte[] bytes) {
        final ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final InflaterInputStream iis = new InflaterInputStream(bais);
        final byte[] buf = new byte[1024];

        try {
            int count = iis.read(buf);
            while (count != -1) {
                baos.write(buf, 0, count);
                count = iis.read(buf);
            }
            return new String(baos.toByteArray());
        } catch (final Exception e) {
            return null;
        } finally {
            try {
                iis.close();
            } catch (final Exception e) {
                // nothing to do
            }
        }
    }

    private static byte[] base64Decode(final String xml) {
        try {
            final byte[] xmlBytes = xml.getBytes("UTF-8");
            return Base64.decodeBase64(xmlBytes);
        } catch (final Exception e) {
            return null;
        }
    }

    private static String inflate(final byte[] bytes) {
        final Inflater inflater = new Inflater(true);
        final byte[] xmlMessageBytes = new byte[10000];

        final byte[] extendedBytes = new byte[bytes.length + 1];
        System.arraycopy(bytes, 0, extendedBytes, 0, bytes.length);
        extendedBytes[bytes.length] = 0;

        inflater.setInput(extendedBytes);

        try {
            final int resultLength = inflater.inflate(xmlMessageBytes);
            inflater.end();

            if (!inflater.finished()) {
                throw new RuntimeException("buffer not large enough.");
            }

            inflater.end();
            return new String(xmlMessageBytes, 0, resultLength, "UTF-8");
        } catch (final DataFormatException e) {
            return null;
        } catch (final UnsupportedEncodingException e) {
            throw new RuntimeException("Cannot find encoding: UTF-8", e);
        }
    }


}
