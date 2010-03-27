package org.jasig.cas.server.authentication;

import org.jasig.services.persondir.IPersonAttributeDao;
import org.jasig.services.persondir.support.StubPersonAttributeDao;

/**
 * Constructs a new {@link org.jasig.cas.server.authentication.JpaAttributePrincipalImpl} using the supplied name.
 * <p>
 * To get around JPA limitations, this factory injects the classes' static variable with the
 * {@link org.jasig.services.persondir.IPersonAttributeDao} so that its always available even after the class is
 * reloaded from the database.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class JpaAttributePrincipalFactory implements AttributePrincipalFactory {

    public JpaAttributePrincipalFactory() {
        JpaAttributePrincipalImpl.setPersonAttributeDao(new StubPersonAttributeDao());
    }

    public AttributePrincipal getAttributePrincipal(final String name) {
        return new JpaAttributePrincipalImpl(name);
    }

    public void setIPersonAttributeDao(final IPersonAttributeDao iPersonAttributeDao) {
        JpaAttributePrincipalImpl.setPersonAttributeDao(iPersonAttributeDao);
    }
}
