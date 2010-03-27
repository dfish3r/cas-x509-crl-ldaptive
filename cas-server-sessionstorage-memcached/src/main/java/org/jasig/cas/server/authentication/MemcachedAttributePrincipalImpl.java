package org.jasig.cas.server.authentication;

import org.jasig.services.persondir.IPersonAttributeDao;

import java.util.List;
import java.util.Map;

/**
 * Memcached-compatible version of {@link org.jasig.cas.server.authentication.AttributePrincipal}
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class MemcachedAttributePrincipalImpl extends AbstractAttributePrincipal {

    private static IPersonAttributeDao IPERSONATTRIBUTEDAO;

    private String name;

    public MemcachedAttributePrincipalImpl(final String name) {
        this.name = name;
    }

    public Map<String, List<Object>> getAttributes() {
        return IPERSONATTRIBUTEDAO.getPerson(this.name).getAttributes();
    }

    public String getName() {
        return this.name;
    }

    public static void setPersonAttributeDao(final IPersonAttributeDao personAttributeDao) {
        IPERSONATTRIBUTEDAO = personAttributeDao;
    }
}
