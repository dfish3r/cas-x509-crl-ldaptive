package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.*;
import org.jasig.cas.server.util.Cleanable;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Abstract class to ensure all session storage are run against the same tests.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSessionStorageTests {

    private final AttributePrincipalFactory attributePrincipalFactory = getAttributePrincipalFactory();

    private final AuthenticationFactory authenticationFactory = getAuthenticationFactory();

    private SessionStorage sessionStorage;

    protected abstract SessionStorage getSessionStorage();

    protected abstract AuthenticationFactory getAuthenticationFactory();

    protected abstract AttributePrincipalFactory getAttributePrincipalFactory();

    protected final Authentication getConstructedAuthentication(final String name) {
        final AttributePrincipal attributePrincipal = this.attributePrincipalFactory.getAttributePrincipal(name);
        return this.authenticationFactory.getAuthentication(attributePrincipal, Collections.<String, List<Object>>emptyMap(), new AuthenticationRequestImpl(Collections.<Credential>emptyList(), false));
    }

    @Before
    public void setUp() throws Exception {
        this.sessionStorage = getSessionStorage();
    }

    @Test
    public final void testCreateSession() {
        final Session session = this.sessionStorage.createSession(getConstructedAuthentication("test"));
        assertNotNull(session);
    }

    @Test
    public final void testDestroySessionThatExists() {
        final Session session = this.sessionStorage.createSession(getConstructedAuthentication("test"));
        assertNotNull(session);

        assertNotNull(this.sessionStorage.findSessionBySessionId(session.getId()));

        assertNotNull(this.sessionStorage.destroySession(session.getId()));
        assertNull(this.sessionStorage.destroySession(session.getId()));
    }

    @Test
    public final void testDestroySessionThatDoesNotExist() {
        assertNull(this.sessionStorage.destroySession("sessionThatDoesNotExist"));
    }

    @Test
    public final void testRetrieveRootSessionThatExists() {
        final Session session = this.sessionStorage.createSession(getConstructedAuthentication("test"));
        assertNotNull(session);

        final Session session2 = this.sessionStorage.findSessionBySessionId(session.getId());
        assertNotNull(session2);
        
        assertEquals(session, session2);
    }

    @Test
    public final void testRetrieveRootSessionThatDoesNotExist() {
        final Session session = this.sessionStorage.createSession(getConstructedAuthentication("test"));
        assertNotNull(session);

        final Session session2 = this.sessionStorage.findSessionBySessionId("FOOBAR");
        assertNull(session2);
    }

    @Test
    public final void testRetrieveSessionsForUserThatDoesNotExist() {
        this.sessionStorage.createSession(getConstructedAuthentication("test"));
        this.sessionStorage.createSession(getConstructedAuthentication("test"));

        final Set<Session> sessions = this.sessionStorage.findSessionsByPrincipal("FOOBAR");

        assertTrue(sessions.isEmpty());
    }

    @Test
    public final void testRetrieveSessionsForUserThatDoestExist() {
        this.sessionStorage.createSession(getConstructedAuthentication("test"));
        this.sessionStorage.createSession(getConstructedAuthentication("test"));
        this.sessionStorage.createSession(getConstructedAuthentication("FOOBAR"));

        assertEquals(2, this.sessionStorage.findSessionsByPrincipal("test").size());
        assertEquals(1, this.sessionStorage.findSessionsByPrincipal("FOOBAR").size());
    }

    // TODO retrieve by access, as well as delegated sessions

    @Test
    public final void testPurge() {
        getSessionStorage().purge();
    }

    @Test
    public final void testCleanable() {
        final SessionStorage sessionStorage = getSessionStorage();

        if (sessionStorage instanceof Cleanable) {
            // TODO do stuff
        }
    }
}
