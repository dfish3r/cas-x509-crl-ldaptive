package org.jasig.cas.server.authentication;

import org.jasig.services.persondir.IPersonAttributeDao;
import org.jasig.services.persondir.support.StubPersonAttributeDao;

import javax.validation.constraints.NotNull;

/**
 * Constructs a new {@link org.jasig.cas.server.authentication.MemcachedAttributePrincipalImpl}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class MemcachedAttributePrincipalFactory implements AttributePrincipalFactory {

    public MemcachedAttributePrincipalFactory() {
        MemcachedAttributePrincipalImpl.setPersonAttributeDao(new StubPersonAttributeDao());
    }

    public AttributePrincipal getAttributePrincipal(final String name) {
        return new MemcachedAttributePrincipalImpl(name);
    }

    public void setPersonAttributeDao(final IPersonAttributeDao personAttributeDao) {
        MemcachedAttributePrincipalImpl.setPersonAttributeDao(personAttributeDao);
    }
}
