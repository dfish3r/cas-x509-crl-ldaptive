package org.jasig.cas.server.session;

import org.jasig.cas.server.authentication.AttributePrincipalFactory;
import org.jasig.cas.server.authentication.AuthenticationFactory;
import org.jasig.cas.server.authentication.InMemoryAttributePrincipalFactoryImpl;
import org.jasig.cas.server.authentication.InMemoryAuthenticationImplFactory;

import java.util.Collections;

/**
 * Concrete test for the {@link org.jasig.cas.server.session.InMemorySessionStorageImpl}
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class InMemorySessionStorageImplTests extends AbstractSessionStorageTests {

    @Override
    protected SessionStorage getSessionStorage() {
        return new InMemorySessionStorageImpl(Collections.<AccessFactory>emptyList());
    }

    @Override
    protected AuthenticationFactory getAuthenticationFactory() {
        return new InMemoryAuthenticationImplFactory();
    }

    @Override
    protected AttributePrincipalFactory getAttributePrincipalFactory() {
        return new InMemoryAttributePrincipalFactoryImpl();
    }
}
