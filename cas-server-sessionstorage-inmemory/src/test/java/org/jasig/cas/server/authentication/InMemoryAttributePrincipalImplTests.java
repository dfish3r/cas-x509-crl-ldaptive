package org.jasig.cas.server.authentication;

import org.jasig.services.persondir.IPersonAttributeDao;

/**
 * Tests for {@link org.jasig.cas.server.authentication.InMemoryAttributePrincipalImpl}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 */
public final class InMemoryAttributePrincipalImplTests extends AbstractAttributePrincipalTests {

    private final InMemoryAttributePrincipalFactoryImpl attributePrincipalFactory = new InMemoryAttributePrincipalFactoryImpl();

    @Override
    protected AttributePrincipal getAttributePrincipal(final String name, final IPersonAttributeDao dao) {
        this.attributePrincipalFactory.setIPersonAttributeDao(dao);
        return this.attributePrincipalFactory.getAttributePrincipal(name);
    }
}
