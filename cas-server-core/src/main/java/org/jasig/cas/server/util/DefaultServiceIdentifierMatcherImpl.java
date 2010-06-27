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

/**
 * Default implementation of a ServiceIdentifierMatcher that removes jsession identifiers from service ids.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class DefaultServiceIdentifierMatcherImpl implements ServiceIdentifierMatcher {

    public boolean matches(final String identifier1, final String identifier2) {
        final String extractedId1 = extractIdForComparison(identifier1);
        final String extractedId2 = extractIdForComparison(identifier2);

        if (extractedId1 == null && extractedId2 == null) {
            return true;
        }

        if (extractedId1 == null || extractedId2 == null) {
            return false;
        }

        return extractedId1.equals(extractedId2);
    }

    /**
     * Extracts the portion of the identifier we wish to compare from the entire String.
     * <p>
     * Default implementation strips off jsessionids.
     *
     * @param identifier the identifier from which to extract the id for comparison.  CANNOT be null.
     * @return the id for comparison.
     */
    protected String extractIdForComparison(final String identifier) {
        if (identifier == null) {
            return null;
        }

        final int jsessionPosition = identifier.indexOf(";jsession");

        if (jsessionPosition == -1) {
            return identifier;
        }

        final int questionMarkPosition = identifier.indexOf("?");

        if (questionMarkPosition < jsessionPosition) {
            return identifier.substring(0, identifier.indexOf(";jsession"));
        }

        return identifier.substring(0, jsessionPosition)
            + identifier.substring(questionMarkPosition);
    }

    public ServiceIdentifierMatcherType getSupportedServiceIdentifierMatcherType() {
        return ServiceIdentifierMatcherType.EXACT;
    }
}
