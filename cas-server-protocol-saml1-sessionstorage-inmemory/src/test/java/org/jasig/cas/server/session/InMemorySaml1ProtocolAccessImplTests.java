package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.AttributePrincipal;
import org.jasig.cas.server.util.Saml1UniqueTicketIdGeneratorImpl;

import static org.mockito.Mockito.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class InMemorySaml1ProtocolAccessImplTests extends AbstractSaml1ProtocolAccessImplTests {

    public InMemorySaml1ProtocolAccessImplTests() throws Exception {
        super();
    }

    @Override
    protected AccessFactory getNewAccessFactory() {
        return new InMemorySaml11ProtocolAccessImplAccessFactory(new Saml1UniqueTicketIdGeneratorImpl("http://www.cnn.com"), "http://www.cnn.com");
    }

    @Override
    protected Session getNewSession() {
        final Session session = mock(Session.class);
        final AttributePrincipal principal = mock(AttributePrincipal.class);
        when(principal.getName()).thenReturn("name");
        when(session.getRootPrincipal()).thenReturn(principal);
        return session;
    }
}
