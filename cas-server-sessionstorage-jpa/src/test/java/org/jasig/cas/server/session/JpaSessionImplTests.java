package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.*;

import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class JpaSessionImplTests extends AbstractSessionTests {

    @Override
    protected String getAuthenticationMethod() {
        return UUID.randomUUID().toString();
    }

    @Override
    protected Session getNewSession(final Authentication authentication, final AttributePrincipal attributePrincipal, final ServicesManager servicesManager) {
        AbstractStaticSession.setExpirationPolicy(getExpirationPolicy());
        AbstractStaticSession.setAccessFactories(getAccessFactories());
        AbstractStaticSession.setServicesManager(servicesManager);
        final DefaultAuthenticationResponseImpl response = new DefaultAuthenticationResponseImpl(new HashSet<Authentication>(Arrays.asList(authentication)), attributePrincipal, Collections.<Credential, List<GeneralSecurityException>>emptyMap(), Collections.<Message>emptyList());
        return new JpaSessionImpl(response);
    }

    @Override
    protected Authentication getNewAuthentication() {
        return new JpaAuthenticationImpl(true, Collections.<String, List<Object>>emptyMap(), UUID.randomUUID().toString());
    }

    @Override
    protected AttributePrincipal getNewAttributePrincipal() {
        return new JpaAttributePrincipalImpl(UUID.randomUUID().toString());
    }

    @Override
    protected Class<? extends Access> getAccessClass() {
        return JpaCasProtocolAccessImpl.class;
    }
}
