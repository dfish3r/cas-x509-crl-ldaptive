package org.jasig.cas.server.session;

import javax.validation.constraints.NotNull;

/**
 * Delegates to different expiration policies depending on whether remember me
 * is true or not.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5.0
 *
 */
public final class LongTermSessionDelegatingExpirationPolicy implements ExpirationPolicy {

    @NotNull
    private final ExpirationPolicy rememberMeExpirationPolicy;

    @NotNull
    private final ExpirationPolicy sessionExpirationPolicy;

    public LongTermSessionDelegatingExpirationPolicy(final ExpirationPolicy rememberMeExpirationPolicy, final ExpirationPolicy sessionExpirationPolicy) {
        this.rememberMeExpirationPolicy = rememberMeExpirationPolicy;
        this.sessionExpirationPolicy = sessionExpirationPolicy;
    }

    public boolean isExpired(final State state) {
        if (!state.longTermAuthenticationExists()) {
            return this.sessionExpirationPolicy.isExpired(state);
        } else {
            return this.rememberMeExpirationPolicy.isExpired(state);
        }
    }
}
