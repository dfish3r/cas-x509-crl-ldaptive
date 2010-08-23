package org.jasig.cas.server.login;

import org.jasig.cas.server.session.Access;
import org.junit.Test;

import java.security.PrivateKey;
import java.security.PublicKey;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 *
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class Saml2ArtifactRequestAccessRequestImplTests {

    private final static String CONST_SESSION_ID = "id";

    private final static String CONST_REMOTE_IP_ADDRESS = "127.0.0.1";

    private final static boolean CONST_FORCE_AUTHENTICATION = true;

    private final static boolean CONST_PASSIVE_AUTHENTICATION = true;

    private final static Access CONST_ACCESS = null;

    private final static String CONST_SERVICE_ID = "serviceId";

    private final static String CONST_REQUST_ID = "requestId";

    private final static String CONST_ALTERNATE_USERNAME = "alternateName";

    private final static String CONST_RELAY_STATE = "relayState";

    private final static PrivateKey CONST_PRIVATE_KEY = mock(PrivateKey.class);

    private final static PublicKey CONST_PUBLIC_KEY = mock(PublicKey.class);

    @Test
    public void testGetters() {

        final Saml2ArtifactRequestAccessRequestImpl impl = new Saml2ArtifactRequestAccessRequestImpl(CONST_SESSION_ID, CONST_REMOTE_IP_ADDRESS, CONST_FORCE_AUTHENTICATION, CONST_PASSIVE_AUTHENTICATION, CONST_ACCESS, CONST_SERVICE_ID, CONST_REQUST_ID, CONST_ALTERNATE_USERNAME, CONST_RELAY_STATE, CONST_PRIVATE_KEY, CONST_PUBLIC_KEY);

        assertEquals(CONST_SESSION_ID, impl.getSessionId());
        assertEquals(CONST_REMOTE_IP_ADDRESS, impl.getRemoteIpAddress());
        assertEquals(CONST_FORCE_AUTHENTICATION, impl.isForceAuthentication());
        assertEquals(CONST_PASSIVE_AUTHENTICATION, impl.isPassiveAuthentication());
        assertEquals(CONST_ACCESS, impl.getOriginalAccess());
        assertEquals(CONST_SERVICE_ID, impl.getServiceId());
        assertEquals(CONST_REQUST_ID, impl.getRequestId());
        assertEquals(CONST_ALTERNATE_USERNAME, impl.getAlternateUserName());
        assertEquals(CONST_RELAY_STATE, impl.getRelayState());
        assertEquals(CONST_PRIVATE_KEY, impl.getPrivateKey());
        assertEquals(CONST_PUBLIC_KEY, impl.getPublicKey());
    }
}
