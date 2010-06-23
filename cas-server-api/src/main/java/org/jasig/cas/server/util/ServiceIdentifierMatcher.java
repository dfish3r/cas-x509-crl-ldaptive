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
 * Determines if two service identifiers match.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface ServiceIdentifierMatcher {

    enum ServiceIdentifierMatcherType {EXACT, REGEX, ANT_PATTERN, CUSTOM}

    /**
     * Determines if two identifiers match, based on some algorithm.
     *
     * @param identifier1 the first identifier
     * @param identifier2  the second identifier.
     * @return true if they do, false otherwise.
     */
    boolean matches(String identifier1, String identifier2);

    /**
     * Returns the type of matching possible with this matcher.
     *
     * @return the type of matching.  CANNOT be NULL.
     */
    ServiceIdentifierMatcherType getSupportedServiceIdentifierMatcherType();
}
