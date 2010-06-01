package org.jasig.cas.server.logout;

import org.junit.Test;

import java.util.Date;

import static org.junit.Assert.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultLogoutResponseImplTests {

    @Test
    public void testEmptyResponse() {
        final LogoutResponse logoutResponse = new DefaultLogoutResponseImpl();
        assertTrue(logoutResponse.getLoggedInAccesses().isEmpty());
        assertTrue(logoutResponse.getLoggedOutAccesses().isEmpty());
        assertNotNull(logoutResponse.getDate());
    }

    // TODO implement this test at some point when I bring in mockito
    public void testNonEmptyResponse() {

    }


    @Test
    public void testImmutability() {
        final LogoutResponse logoutResponse = new DefaultLogoutResponseImpl();
        assertNotNull(logoutResponse.getDate());

         final Date date = logoutResponse.getDate();
         date.setTime(500);

         assertNotSame(date, logoutResponse.getDate());
    }


}
