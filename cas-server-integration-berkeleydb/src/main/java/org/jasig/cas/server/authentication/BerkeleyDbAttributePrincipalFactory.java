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
import org.jasig.services.persondir.support.StubPersonAttributeDao;

/**
 * Factory to construct new BerkeleyDb-friendly instances of the AttributePrincipal.
 *
 * @version $Revision$ $Date$
 * @since 3.5.0
 */
public final class BerkeleyDbAttributePrincipalFactory implements AttributePrincipalFactory {

    public BerkeleyDbAttributePrincipalFactory() {
        this(new StubPersonAttributeDao());
    }

    public BerkeleyDbAttributePrincipalFactory(final IPersonAttributeDao personAttributeDao) {
        BerkeleyDbAttributePrincipalImpl.setPersonAttributeDao(personAttributeDao);
    }

    public AttributePrincipal getAttributePrincipal(final String name) {
        return new BerkeleyDbAttributePrincipalImpl(name);
    }
}
