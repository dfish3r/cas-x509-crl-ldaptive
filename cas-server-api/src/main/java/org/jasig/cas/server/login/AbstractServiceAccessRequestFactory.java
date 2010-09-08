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
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 1.0.0
 * User: scottbattaglia
 * Date: Aug 10, 2010
 * Time: 10:13:44 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class AbstractServiceAccessRequestFactory implements ServiceAccessRequestFactory {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    protected final String getValue(Object o) {
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
