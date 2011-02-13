package org.jasig.cas.server.authentication;

import org.jasig.cas.adaptors.jdbc.SearchModeSearchDatabaseAuthenticationHandler;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class SearchModeSearchDatabaseAuthenticationHandlerTests extends AbstractJdbcTests {

    @Test
    public void users() throws Exception {
        final SearchModeSearchDatabaseAuthenticationHandler a = new SearchModeSearchDatabaseAuthenticationHandler(getDataSource());
        a.setFieldPassword("password");
        a.setFieldUser("user_name");
        a.setTableUsers("users");
        a.afterPropertiesSet();

        final DefaultUserNamePasswordCredential validUser = new DefaultUserNamePasswordCredential("foo", "bar");
        final DefaultUserNamePasswordCredential invalidPassword = new DefaultUserNamePasswordCredential("foo", "ha");
        final DefaultUserNamePasswordCredential invalidUser = new DefaultUserNamePasswordCredential("nope", "ha");

        assertTrue(a.authenticate(validUser));
        assertFalse(a.authenticate(invalidPassword));
        assertFalse(a.authenticate(invalidUser));
    }
}
