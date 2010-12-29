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

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

/**
 * Constructs a new {@link org.jasig.cas.server.authentication.JpaAttributePrincipalImpl} using the supplied name.
 * <p>
 * To get around JPA limitations, this factory injects the classes' static variable with the
 * {@link org.jasig.services.persondir.IPersonAttributeDao} so that its always available even after the class is
 * reloaded from the database.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@Named("attributePrincipalFactory")
@Singleton
public final class JpaAttributePrincipalFactory extends AbstractStaticAttributePrincipalImplFactory {

    @Inject
    public JpaAttributePrincipalFactory(final IPersonAttributeDao iPersonAttributeDao) {
        super(iPersonAttributeDao);
    }

    public AttributePrincipal getAttributePrincipal(final String name) {
        return new JpaAttributePrincipalImpl(name);
    }
}
