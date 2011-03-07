package org.jasig.cas.server.session;

import org.jasig.cas.server.Saml11Profile;
import org.jasig.cas.server.login.Saml11RequestAccessRequestImpl;
import org.jasig.cas.server.login.ServiceAccessRequest;
import org.junit.Test;
import org.opensaml.DefaultBootstrap;

import java.io.StringWriter;

import static org.junit.Assert.*;

import static org.mockito.Mockito.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public abstract class AbstractSaml1ProtocolAccessImplTests {

    protected abstract AccessFactory getNewAccessFactory();

    protected abstract Session getNewSession();

    public AbstractSaml1ProtocolAccessImplTests() throws Exception {
        DefaultBootstrap.bootstrap();
    }

    @Test
    public final void testGenerateAnAccess() {
        final Saml11RequestAccessRequestImpl request = new Saml11RequestAccessRequestImpl(null, "127.0.0.1", false, false, "http://www.cnn.com", Saml11Profile.BrowserArtifact);
        assertNotNull(getNewAccessFactory().getAccess(getNewSession(), request));
    }

    @Test
    public final void testDontGenerateAnAccess() {
        final ServiceAccessRequest request = mock(ServiceAccessRequest.class);
        assertNull(getNewAccessFactory().getAccess(getNewSession(), request));
    }

    @Test
    public final void testGetters() {
        final Saml11RequestAccessRequestImpl request = new Saml11RequestAccessRequestImpl(null, "127.0.0.1", false, false, "http://www.cnn.com", Saml11Profile.BrowserArtifact);
        final AbstractSaml1ProtocolAccessImpl access = (AbstractSaml1ProtocolAccessImpl) getNewAccessFactory().getAccess(getNewSession(), request);
        assertNotNull(access.getId());
        assertEquals(request.getProfile(), access.getProfile());
        assertEquals(request.getServiceId(), access.getResourceIdentifier());
        assertNull(access.getRequestId());
    }

    @Test
    public final void testInitialRedirect() {
        final Saml11RequestAccessRequestImpl request = new Saml11RequestAccessRequestImpl(null, "127.0.0.1", false, false, "http://www.cnn.com", Saml11Profile.BrowserArtifact);
        final AbstractSaml1ProtocolAccessImpl access = (AbstractSaml1ProtocolAccessImpl) getNewAccessFactory().getAccess(getNewSession(), request);
        final DefaultAccessResponseRequestImpl impl = new DefaultAccessResponseRequestImpl(new StringWriter());
        final AccessResponseResult accessResponseResult = access.generateResponse(impl);
        assertEquals(AccessResponseResult.Operation.REDIRECT, accessResponseResult.getOperationToPerform());
    }

    @Test
    public final void testInitialPostRedirect() {
        final Saml11RequestAccessRequestImpl request = new Saml11RequestAccessRequestImpl(null, "127.0.0.1", false, false, "http://www.cnn.com", Saml11Profile.BrowserPost);
        final AbstractSaml1ProtocolAccessImpl access = (AbstractSaml1ProtocolAccessImpl) getNewAccessFactory().getAccess(getNewSession(), request);
        final DefaultAccessResponseRequestImpl impl = new DefaultAccessResponseRequestImpl(new StringWriter());
        final AccessResponseResult accessResponseResult = access.generateResponse(impl);
        assertEquals(AccessResponseResult.Operation.POST, accessResponseResult.getOperationToPerform());
        assertEquals(request.getServiceId(), accessResponseResult.getUrl());
    }
}
