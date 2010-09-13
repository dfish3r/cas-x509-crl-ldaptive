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

package org.jasig.cas.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;

/**
 * Utilities adopted from the Google sample code.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1
 */
public final class SamlUtils {

    private static final DocumentBuilderFactory DOCUMENT_BUILDER_FACTORY = DocumentBuilderFactory.newInstance();

    private static final Logger logger = LoggerFactory.getLogger(SamlUtils.class);

    private SamlUtils() {
        // nothing to do
    }

    /**
     * Constructs a new W3C Document object from the provided XML string.  If it can't construct one, it will return null.
     *
     * @param xmlString the string to parse.
     * @return the object, or null if it could not be constructed.
     */
    public static Document constructDocumentFromXmlString(final String xmlString) {
        try {
            final DocumentBuilder documentBuilder = DOCUMENT_BUILDER_FACTORY.newDocumentBuilder();
            return documentBuilder.parse(new ByteArrayInputStream(xmlString.getBytes()));
        } catch (final Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static String createStringFromDocument(final Document document) {
        try {
            final TransformerFactory transformerFactory = TransformerFactory.newInstance();
            final Transformer trans = transformerFactory.newTransformer();
            trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            trans.setOutputProperty(OutputKeys.INDENT, "yes");

            //create string from xml tree
            final StringWriter sw = new StringWriter();
            final StreamResult result = new StreamResult(sw);
            final DOMSource source = new DOMSource(document);
            trans.transform(source, result);
            return sw.toString();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String createStringFromJaxBClass(final Class clazz, final Object o) {
        try {
            final JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            final Marshaller marshaller = jaxbContext.createMarshaller();
            final StringWriter writer = new StringWriter();
            marshaller.marshal(o, writer);
            return writer.toString();
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
