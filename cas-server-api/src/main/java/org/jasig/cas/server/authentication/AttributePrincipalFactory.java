package org.jasig.cas.server.authentication;

/**
 * Abstracts constructing a new {@link AttributePrincipal} so that one can be created that is compliant with the backing
 * storage mechanism.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface AttributePrincipalFactory {

    /**
     * Constructs a new AttributePrincipal based on the supplied values.
     *
     * @param name the name of the principal.  CANNOT be NULL.
     * @return the principal.
     */
    AttributePrincipal getAttributePrincipal(String name);
}
