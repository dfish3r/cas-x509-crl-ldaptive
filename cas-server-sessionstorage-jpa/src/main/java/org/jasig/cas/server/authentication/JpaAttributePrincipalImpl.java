package org.jasig.cas.server.authentication;

import org.jasig.services.persondir.IPersonAttributeDao;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

/**
 * JPA compliant implementation of the Principal.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
@Embeddable
public final class JpaAttributePrincipalImpl extends AbstractAttributePrincipal {

    private static IPersonAttributeDao IPERSONATTRIBUTEDAO;

    @Column(name="princ_name",nullable = false, insertable = true, length=256, updatable = false)
    private String name;

    public JpaAttributePrincipalImpl() {
        // this is for JPA
    }

    public JpaAttributePrincipalImpl(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public Map<String, List<Object>> getAttributes() {
        return IPERSONATTRIBUTEDAO.getPerson(this.name).getAttributes();
    }

    public static void setPersonAttributeDao(final IPersonAttributeDao personAttributeDao) {
        IPERSONATTRIBUTEDAO = personAttributeDao;
    }
}
