package org.jasig.cas.server.authentication;

import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Immutable version of the {@link org.jasig.cas.server.authentication.Authentication} interface that can easily
 * be stored in-memory.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class InMemoryAuthenticationImpl implements Authentication, Serializable {

    private final Date date = new Date();

    private final AttributePrincipal principal;

    private final Map<String, List<Object>> authenticationMetaData;

    private final boolean longTermAuthentication;

    public InMemoryAuthenticationImpl(final AttributePrincipal principal, final Map<String, List<Object>> authenticationMetaData, final boolean longTermAuthentication) {
        this.principal = principal;
        this.authenticationMetaData = Collections.unmodifiableMap(authenticationMetaData);
        this.longTermAuthentication = longTermAuthentication;
    }

    public Date getAuthenticationDate() {
        return new Date(this.date.getTime());
    }

    public Map<String, List<Object>> getAuthenticationMetaData() {
        return this.authenticationMetaData;
    }

    public AttributePrincipal getPrincipal() {
        return this.principal;
    }

    public boolean isLongTermAuthentication() {
        return this.longTermAuthentication;
    }
}
