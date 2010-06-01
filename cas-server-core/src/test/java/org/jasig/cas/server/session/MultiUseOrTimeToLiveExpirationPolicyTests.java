package org.jasig.cas.server.session;

import org.junit.Test;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class MultiUseOrTimeToLiveExpirationPolicyTests extends AbstractExpirationPolicyTests {

    public MultiUseOrTimeToLiveExpirationPolicyTests() {
        super(new MultiUseOrTimeToLiveExpirationPolicy(2, 5));
    }

    @Test
    public void testBeforeExpirationTime() {
        final State state = new SimpleStateImpl();
        state.updateState();
        sleep(3);
        assertFalse(getExpirationPolicy().isExpired(state));
    }

    @Test
    public void testAfterExpirationTime() {
        final State state = new SimpleStateImpl();
        state.updateState();
        sleep(7);
        assertTrue(getExpirationPolicy().isExpired(state));
    }

    @Test
    public void testAfterTooManyUses() {
        final State state = new SimpleStateImpl();

        for (int i = 0; i < 3; i++) {
            state.updateState();
        }

        assertTrue(getExpirationPolicy().isExpired(state));
    }
}
