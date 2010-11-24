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

import org.jasig.services.persondir.IPersonAttributeDao;
import org.jasig.services.persondir.IPersonAttributes;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Abstract implementation of the AttributePrincipal interface that implements some of the basic functionality.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractAttributePrincipal implements AttributePrincipal {


    public final List<Object> getAttributeValues(final String attribute) {
        final IPersonAttributes attributes = getPersonAttributeDao().getPerson(getName());

        if (attributes == null) {
            return null;
        }

        return attributes.getAttributeValues(attribute);
    }

    public final Object getAttributeValue(final String attribute) {
        final IPersonAttributes attributes = getPersonAttributeDao().getPerson(getName());

        if (attributes == null) {
            return null;
        }

        return attributes.getAttributeValue(attribute);
    }

    public final Map<String, List<Object>> getAttributes() {
        final IPersonAttributes attributes =  getPersonAttributeDao().getPerson(getName());

        if (attributes == null) {
            return Collections.emptyMap();
        }

        return attributes.getAttributes();
    }

    protected abstract IPersonAttributeDao getPersonAttributeDao();
}
