package org.jasig.cas.server.authentication;

import org.jasig.services.persondir.IPersonAttributeDao;

import java.util.List;
import java.util.Map;

/**
 * A principal that's safe to store in-memory.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class InMemoryAttributePrincipalImpl extends AbstractAttributePrincipal {

    private final String name;

    private IPersonAttributeDao iPersonAttributeDao;

    public InMemoryAttributePrincipalImpl(final String name, final IPersonAttributeDao iPersonAttributeDao) {
        this.name = name;
        this.iPersonAttributeDao = iPersonAttributeDao;
    }

    public Map<String, List<Object>> getAttributes() {
        return this.iPersonAttributeDao.getPerson(this.name).getAttributes();
    }

    public String getName() {
        return this.name;
    }
}
