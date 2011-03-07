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

package org.jasig.cas.server.session;

import org.jasig.cas.server.CasProtocolVersion;
import org.jasig.cas.server.login.CasServiceAccessRequestImpl;
import org.jasig.cas.server.login.CasTokenServiceAccessRequestImpl;
import org.junit.Test;

import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public abstract class AbstractCasProtocolAccessImplTests {

    protected abstract AbstractCasProtocolAccessImplFactory getCasProtocolAccessImplFactory();

    protected final CasServiceAccessRequestImpl getNewCasServiceAccessRequest(final boolean post) {
        return new CasServiceAccessRequestImpl("sessionId", "127.0.0.1", false, false, "http://www.cnn.com", post);
    }

    protected abstract Session getNewSession();

    @Test
    public final void testCreateInstance() {
        final Session session = getNewSession();
        final Access access = getCasProtocolAccessImplFactory().getAccess(session, getNewCasServiceAccessRequest(false));
        assertNotNull(access);
    }

    @Test
    public final void testGetters() {
        final Session session = getNewSession();
        final CasServiceAccessRequestImpl serviceAccessRequest = getNewCasServiceAccessRequest(false);
        final Access access = getCasProtocolAccessImplFactory().getAccess(session, serviceAccessRequest);
        assertNotNull(access);
        assertNotNull(access.getId());
        assertEquals(serviceAccessRequest.getServiceId(), access.getResourceIdentifier());
    }

    @Test
    public final void testGetGenerateResponse() {
        final Session session = getNewSession();
        final CasServiceAccessRequestImpl serviceAccessRequest = getNewCasServiceAccessRequest(false);
        final Access access = getCasProtocolAccessImplFactory().getAccess(session, serviceAccessRequest);
        final StringWriter writer = new StringWriter();
        final AccessResponseResult accessResponseResult = access.generateResponse(new DefaultAccessResponseRequestImpl(writer));
        assertEquals(AccessResponseResult.Operation.REDIRECT, accessResponseResult.getOperationToPerform());
        assertTrue(accessResponseResult.getUrl().contains(access.getId()));
        assertTrue(accessResponseResult.getUrl().contains(access.getResourceIdentifier()));
    }

    @Test
    public final void testPostGenerateResponse() {
        final Session session = getNewSession();
        final CasServiceAccessRequestImpl serviceAccessRequest = getNewCasServiceAccessRequest(true);
        final Access access = getCasProtocolAccessImplFactory().getAccess(session, serviceAccessRequest);
        final StringWriter writer = new StringWriter();
        final AccessResponseResult accessResponseResult = access.generateResponse(new DefaultAccessResponseRequestImpl(writer));
        assertEquals(AccessResponseResult.Operation.POST, accessResponseResult.getOperationToPerform());
    }

    @Test
    public final void testSuccessValidation() {
        final Session session = getNewSession();
        final CasServiceAccessRequestImpl serviceAccessRequest = getNewCasServiceAccessRequest(true);
        final Access access = getCasProtocolAccessImplFactory().getAccess(session, serviceAccessRequest);
        final CasTokenServiceAccessRequestImpl impl = new CasTokenServiceAccessRequestImpl(CasProtocolVersion.CAS2, access.getId(), access.getResourceIdentifier(), "127.0.0.1", false);
        access.validate(impl);
        final StringWriter writer = new StringWriter();
        final AccessResponseResult accessResponseResult = access.generateResponse(new DefaultAccessResponseRequestImpl(writer));
        assertEquals("casServiceSuccessView", accessResponseResult.getViewName());
    }

    @Test
    public final void testMismatchedResource() {
        final Session session = getNewSession();
        final CasServiceAccessRequestImpl serviceAccessRequest = getNewCasServiceAccessRequest(true);
        final Access access = getCasProtocolAccessImplFactory().getAccess(session, serviceAccessRequest);
        final CasTokenServiceAccessRequestImpl impl = new CasTokenServiceAccessRequestImpl(CasProtocolVersion.CAS2, access.getId(), "http://www.jasig.org", "127.0.0.1", false);
        access.validate(impl);
        final StringWriter writer = new StringWriter();
        final AccessResponseResult accessResponseResult = access.generateResponse(new DefaultAccessResponseRequestImpl(writer));
        assertEquals("casServiceFailureView", accessResponseResult.getViewName());
    }

    @Test
    public final void testResponseTwice() {
        final Session session = getNewSession();
        final CasServiceAccessRequestImpl serviceAccessRequest = getNewCasServiceAccessRequest(true);
        final Access access = getCasProtocolAccessImplFactory().getAccess(session, serviceAccessRequest);
        final CasTokenServiceAccessRequestImpl impl = new CasTokenServiceAccessRequestImpl(CasProtocolVersion.CAS2, access.getId(), access.getResourceIdentifier(), "127.0.0.1", false);
        access.validate(impl);
        final StringWriter writer = new StringWriter();
        final AccessResponseResult accessResponseResult = access.generateResponse(new DefaultAccessResponseRequestImpl(writer));
        assertEquals("casServiceSuccessView", accessResponseResult.getViewName());
        final AccessResponseResult accessResponseResult2 = access.generateResponse(new DefaultAccessResponseRequestImpl(writer));
        assertEquals("casServiceFailureView", accessResponseResult2.getViewName());
    }

    @Test
    public final void testValidateTwice() {
        final Session session = getNewSession();
        final CasServiceAccessRequestImpl serviceAccessRequest = getNewCasServiceAccessRequest(true);
        final Access access = getCasProtocolAccessImplFactory().getAccess(session, serviceAccessRequest);
        final CasTokenServiceAccessRequestImpl impl = new CasTokenServiceAccessRequestImpl(CasProtocolVersion.CAS2, access.getId(), access.getResourceIdentifier(), "127.0.0.1", false);
        access.validate(impl);
        access.validate(impl);
        final StringWriter writer = new StringWriter();
        final AccessResponseResult accessResponseResult2 = access.generateResponse(new DefaultAccessResponseRequestImpl(writer));
        assertEquals("casServiceFailureView", accessResponseResult2.getViewName());
    }
}
