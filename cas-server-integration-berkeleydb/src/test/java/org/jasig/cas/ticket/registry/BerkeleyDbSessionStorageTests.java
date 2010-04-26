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

package org.jasig.cas.ticket.registry;

import java.io.File;
import java.util.*;

import org.jasig.cas.server.authentication.AttributePrincipalFactory;
import org.jasig.cas.server.authentication.AuthenticationFactory;
import org.jasig.cas.server.authentication.BerkeleyDbAttributePrincipalFactory;
import org.jasig.cas.server.authentication.BerkeleyDbAuthenticationFactory;
import org.jasig.cas.server.session.AbstractSessionStorageTests;
import org.jasig.cas.server.session.AccessFactory;
import org.jasig.cas.server.session.BerkeleyDbSessionStorageImpl;
import org.jasig.cas.server.session.SessionStorage;

/**
 * 
 * @author Andres March
 * @version $Revision$ $Date$
 * @since 3.1
 *
 */
public class BerkeleyDbSessionStorageTests extends AbstractSessionStorageTests {

    private BerkeleyDbSessionStorageImpl reg;

    @Override
    protected SessionStorage getSessionStorage() {
        try {
            this.reg = new BerkeleyDbSessionStorageImpl(Collections.<AccessFactory>emptyList());
            return reg;
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected AuthenticationFactory getAuthenticationFactory() {
        return new BerkeleyDbAuthenticationFactory();
    }

    @Override
    protected AttributePrincipalFactory getAttributePrincipalFactory() {
        return new BerkeleyDbAttributePrincipalFactory();
    }


	protected void tearDown() throws Exception {
		this.reg.destroy();
		this.reg = null;
		new File("00000000.jdb").delete();
		new File("je.lck").delete();
	}
}
