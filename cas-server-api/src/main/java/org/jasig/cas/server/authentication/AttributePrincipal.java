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

package org.jasig.cas.server.authentication;

import java.io.Serializable;
import java.security.Principal;
import java.util.List;
import java.util.Map;

/**
 * Extension to the Java principal that includes attribute information.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public interface AttributePrincipal extends Principal, Serializable {


    /**
     * Returns all of the attribute values associated with this attribute or NULL if none exist.
     *
     * @param attribute the attribute values to return. CANNOT be NULL.
     * @return the list of attribute values, or NULL.
     */
    List<Object> getAttributeValues(String attribute);

    /**
     * Returns the first value for the specified attribute. Returns NULL if there is no attribute, or if the first one
     * is null
     *
     * @param attribute the attribute value to find.  CANNOT be NULL.
     * @return the value, or NULL if it doesn't exist.
     */
    Object getAttributeValue(String attribute);

    /**
     * The map of the attributes about a particular principal.  The attributes are keyed on a String and can return a list
     * of objects that may be one or more.  An attribute should not be included in the map if it has no values.
     *
     * @return the map of attributes.  The map CAN be empty but CANNOT be null.
     */
    Map<String, List<Object>> getAttributes();
}
