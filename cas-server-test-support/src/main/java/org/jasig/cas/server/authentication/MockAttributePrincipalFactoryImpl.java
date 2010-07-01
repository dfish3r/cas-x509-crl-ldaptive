package org.jasig.cas.server.authentication;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class MockAttributePrincipalFactoryImpl implements AttributePrincipalFactory {

    public AttributePrincipal getAttributePrincipal(final String name) {
        return new AttributePrincipal() {
            public List<Object> getAttributeValues(String attribute) {
                return null;
            }

            public Object getAttributeValue(String attribute) {
                return null;
            }

            public Map<String, List<Object>> getAttributes() {
                return Collections.<String, List<Object>>emptyMap();
            }

            public String getName() {
                return name;
            }
        };
    }
}
