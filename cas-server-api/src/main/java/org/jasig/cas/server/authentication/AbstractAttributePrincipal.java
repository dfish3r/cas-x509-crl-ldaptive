package org.jasig.cas.server.authentication;

import java.util.List;

/**
 * Abstract implementation of the AttributePrincipal interface that implements some of the basic functionality.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractAttributePrincipal implements AttributePrincipal {


    public final List<Object> getAttributeValues(final String attribute) {
        final List<Object> attributes = getAttributes().get(attribute);

        if (attributes == null || attributes.isEmpty()) {
            return null;
        }

        return attributes;
    }

    public final Object getAttributeValue(final String attribute) {
        final List<Object> attributes = getAttributeValues(attribute);

        return attributes == null ? null : attributes.get(0);
    }
}
