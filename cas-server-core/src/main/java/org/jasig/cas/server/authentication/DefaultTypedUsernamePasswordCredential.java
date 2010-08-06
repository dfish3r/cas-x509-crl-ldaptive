package org.jasig.cas.server.authentication;

/**
 * Default implementation of {@link org.jasig.cas.server.authentication.TypedUsernamePasswordCredential}.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class DefaultTypedUsernamePasswordCredential extends DefaultUserNamePasswordCredential implements TypedUsernamePasswordCredential {

    private String type;

    public final String getType() {
        return this.type;
    }

    public final void setType(final String type) {
        this.type = type;
    }
}
