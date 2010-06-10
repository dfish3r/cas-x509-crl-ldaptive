package org.jasig.cas.server.authentication;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractAuthenticationManager implements AuthenticationManager {

    @NotNull
    private final AuthenticationFactory authenticationFactory;

    @NotNull
    private List<AuthenticationMetaDataResolver> authenticationMetaDataResolvers = new ArrayList<AuthenticationMetaDataResolver>();

    @NotNull
    private List<MessageResolver> messageResolvers = new ArrayList<MessageResolver>();

    private boolean allCredentialsMustSucceed = true;

    protected AbstractAuthenticationManager(final AuthenticationFactory authenticationFactory) {
        this.authenticationFactory = authenticationFactory;
    }

    public final List<AuthenticationMetaDataResolver> getAuthenticationMetaDataResolvers() {
        return this.authenticationMetaDataResolvers;
    }

    public final void setAuthenticationMetaDataResolvers(final List<AuthenticationMetaDataResolver> authenticationMetaDataResolvers) {
        this.authenticationMetaDataResolvers = authenticationMetaDataResolvers;
    }

    protected final List<MessageResolver> getMessageResolvers() {
        return this.messageResolvers;
    }

    public final void setMessageResolvers(final List<MessageResolver> messageResolvers) {
        this.messageResolvers = messageResolvers;
    }

    protected final boolean isAllCredentialsMustSucceed() {
        return this.allCredentialsMustSucceed;
    }

    public final void setAllCredentialsMustSucceed(final boolean allCredentialsMustSucceed) {
        this.allCredentialsMustSucceed = allCredentialsMustSucceed;
    }

    protected final AuthenticationFactory getAuthenticationFactory() {
        return this.authenticationFactory;
    }
}
