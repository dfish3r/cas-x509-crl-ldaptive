package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.*;
import org.jasig.cas.server.util.Saml1UniqueTicketIdGeneratorImpl;

import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class SerializableSaml1ProtocolAccessImplTests extends AbstractSaml1ProtocolAccessImplTests {

    public SerializableSaml1ProtocolAccessImplTests() throws Exception {
        super();
    }

    @Override
    protected AccessFactory getNewAccessFactory() {
        AbstractStaticSaml1ProtocolAccessImpl.setExpirationPolicy(new NeverExpiresExpirationPolicy());
        return new SerializableSaml11ProtocolAccessImplFactory(new Saml1UniqueTicketIdGeneratorImpl("test"), "test");
    }

    @Override
    protected Session getNewSession() {
        AbstractStaticSession.setExpirationPolicy(new NeverExpiresExpirationPolicy());
        final DefaultAuthenticationResponseImpl impl = new DefaultAuthenticationResponseImpl(
                new HashSet<Authentication>(Arrays.asList(new SerializableAuthenticationImpl(Collections.<String, List<Object>>emptyMap(), false, "password"))),
                new SerializableAttributePrincipalImpl("duh"),
                Collections.<Credential, List<GeneralSecurityException>>emptyMap(),
                Collections.<Message>emptyList());
        final SerializableSessionImpl session = new SerializableSessionImpl(impl);
        return session;
    }


}
