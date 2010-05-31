package org.jasig.cas.server.authentication;

/**
 * Default, simple, mutable version of the {@link org.jasig.cas.server.authentication.UserNamePasswordCredential}
 * interface.  Suitable for things like Spring Binding.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class DefaultUserNamePasswordCredential implements UserNamePasswordCredential {

    private String userName;

    private String password;

    public final void setUserName(final String userName) {
        this.userName = userName;
    }

    public final void setPassword(final String password) {
        this.password = password;
    }

    public final String getUserName() {
        return userName;
    }

    public final String getPassword() {
        return password;
    }
}
