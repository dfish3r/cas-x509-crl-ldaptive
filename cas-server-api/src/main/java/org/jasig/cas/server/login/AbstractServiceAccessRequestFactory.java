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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * Abstract representation of of {@link ServiceAccessRequestFactory} that provides a method to help retrieve items from
 * request parameters.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 1.0.0
 */
public abstract class AbstractServiceAccessRequestFactory implements ServiceAccessRequestFactory {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Retrieve the first value from an array, or the toString version of an object if its not an array.
     *
     * @param o the object to get the value of.
     * @return the first String if its a String[] or o.toString().
     */
    protected final String getValue(final Object o) {
        if (o == null) {
            return null;
        }

        if (o instanceof String[]) {
            final String[] s = (String[]) o;
            return s[0];
        }

        return o.toString();
    }
}
