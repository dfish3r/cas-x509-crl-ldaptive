package org.jasig.cas.server.login;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class CasServiceAccessRequestImplTests {

    @Test
    public void gettersAndSetters() {
        final String CONST_SESSION_ID = "sessionId";
        final String CONST_SERVICE_ID = "constServiceId";
        final String CONST_REMOTE_IP = "127.0.0.1";
        final CasServiceAccessRequestImpl casServiceAccessRequest = new CasServiceAccessRequestImpl(CONST_SESSION_ID, CONST_SERVICE_ID, CONST_REMOTE_IP);
        assertEquals(CONST_SESSION_ID, casServiceAccessRequest.getSessionId());
        assertEquals(CONST_SERVICE_ID, casServiceAccessRequest.getServiceId());
        assertEquals(CONST_REMOTE_IP, casServiceAccessRequest.getRemoteIpAddress());
        assertFalse(casServiceAccessRequest.isPassiveAuthentication());
        assertFalse(casServiceAccessRequest.isPostRequest());
        assertTrue(casServiceAccessRequest.isProxiedRequest());
        assertFalse(casServiceAccessRequest.isForceAuthentication());
        assertFalse(casServiceAccessRequest.isLongTermLoginRequest());
        assertTrue(casServiceAccessRequest.isValid());
        assertTrue(casServiceAccessRequest.isAccessRequest());
    }

    @Test
    public void equalsAndHashcode() {
        final String CONST_SESSION_ID = "sessionId";
        final String CONST_SERVICE_ID = "constServiceId";
        final String CONST_REMOTE_IP = "127.0.0.1";
        final CasServiceAccessRequestImpl casServiceAccessRequest = new CasServiceAccessRequestImpl(CONST_SESSION_ID, CONST_SERVICE_ID, CONST_REMOTE_IP);
        final CasServiceAccessRequestImpl casServiceAccessRequest2 = new CasServiceAccessRequestImpl(CONST_SESSION_ID, CONST_SERVICE_ID, CONST_REMOTE_IP);
        assertEquals(casServiceAccessRequest, casServiceAccessRequest2);
        assertEquals(casServiceAccessRequest.hashCode(), casServiceAccessRequest2.hashCode());
    }
}
