package org.jasig.cas.server.authentication;

import org.jasig.services.persondir.IPersonAttributeDao;
import org.jasig.services.persondir.support.StubPersonAttributeDao;

import javax.validation.constraints.NotNull;

/**
 * Constructs a new {@link org.jasig.cas.server.authentication.InMemoryAttributePrincipalImpl}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class InMemoryAttributePrincipalFactoryImpl implements AttributePrincipalFactory {

    @NotNull
    private IPersonAttributeDao iPersonAttributeDao = new StubPersonAttributeDao();

    public AttributePrincipal getAttributePrincipal(final String name) {
        return new InMemoryAttributePrincipalImpl(name, this.iPersonAttributeDao);
    }
    
    public void setIPersonAttributeDao(final IPersonAttributeDao iPersonAttributeDao) {
        this.iPersonAttributeDao = iPersonAttributeDao;
    }
}
