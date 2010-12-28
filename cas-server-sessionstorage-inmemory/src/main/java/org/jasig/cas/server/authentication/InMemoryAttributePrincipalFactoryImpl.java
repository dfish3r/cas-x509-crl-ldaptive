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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.inject.Named;
import javax.inject.Singleton;
import javax.validation.constraints.NotNull;

/**
 * Constructs a new {@link org.jasig.cas.server.authentication.InMemoryAttributePrincipalImpl}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
@Named("attributePrincipalFactory")
@Singleton
public final class InMemoryAttributePrincipalFactoryImpl implements AttributePrincipalFactory {

    @NotNull
    @Autowired(required=false)
    private IPersonAttributeDao iPersonAttributeDao = new StubPersonAttributeDao();

    public AttributePrincipal getAttributePrincipal(final String name) {
        return new InMemoryAttributePrincipalImpl(name, this.iPersonAttributeDao);
    }
    
    public void setIPersonAttributeDao(final IPersonAttributeDao iPersonAttributeDao) {
        this.iPersonAttributeDao = iPersonAttributeDao;
    }
}
