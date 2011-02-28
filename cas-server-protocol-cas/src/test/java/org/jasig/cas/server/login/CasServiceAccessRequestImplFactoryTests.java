package org.jasig.cas.server.login;

import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class CasServiceAccessRequestImplFactoryTests {

    private CasServiceAccessRequestImplFactory factory = new CasServiceAccessRequestImplFactory();

    @Test
    public void getAccess() {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        final String CONST_SESSION_ID = "sessionId";
        final String CONST_REMOTE_IP = "127.0.0.1";
        request.setParameter("renew", "true");
//        request.setParameter("gateway", "false");
        request.setParameter("service", "http://www.cnn.com");

        final CasServiceAccessRequestImpl casServiceAccessRequest = (CasServiceAccessRequestImpl) factory.getServiceAccessRequest(CONST_SESSION_ID, CONST_REMOTE_IP, request.getParameterMap());
        assertTrue(casServiceAccessRequest.isForceAuthentication());
        assertFalse(casServiceAccessRequest.isPassiveAuthentication());
        assertEquals("http://www.cnn.com", casServiceAccessRequest.getServiceId());
        assertEquals(CONST_REMOTE_IP, casServiceAccessRequest.getRemoteIpAddress());
        assertEquals(CONST_SESSION_ID, casServiceAccessRequest.getSessionId());
    }
}
