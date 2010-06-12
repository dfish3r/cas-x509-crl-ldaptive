package org.jasig.cas.server.authentication;

import com.github.inspektr.audit.annotation.Audit;
import org.perf4j.aop.Profiled;

import javax.validation.constraints.NotNull;
import java.security.GeneralSecurityException;
import java.util.*;

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

    public final void setAuthenticationMetaDataResolvers(final List<AuthenticationMetaDataResolver> authenticationMetaDataResolvers) {
        this.authenticationMetaDataResolvers = authenticationMetaDataResolvers;
    }

    public final void setMessageResolvers(final List<MessageResolver> messageResolvers) {
        this.messageResolvers = messageResolvers;
    }

    public final void setAllCredentialsMustSucceed(final boolean allCredentialsMustSucceed) {
        this.allCredentialsMustSucceed = allCredentialsMustSucceed;
    }

    protected final AuthenticationFactory getAuthenticationFactory() {
        return this.authenticationFactory;
    }

    @Profiled(tag="defaultAuthenticationManager_authenticate")
    @Audit(action="AUTHENTICATION", actionResolverName="AUTHENTICATION_RESOLVER", resourceResolverName="AUTHENTICATION_RESOURCE_RESOLVER")
    public final AuthenticationResponse authenticate(final AuthenticationRequest authenticationRequest) {
        final List<Credential> credentials = new ArrayList<Credential>();
        final Set<Authentication> authentications = new HashSet<Authentication>();
        final List<AttributePrincipal> principals = new ArrayList<AttributePrincipal>();
        final List<GeneralSecurityException> exceptions = new ArrayList<GeneralSecurityException>();
        final List<Message> messages = new ArrayList<Message>();

        obtainAuthenticationsAndPrincipals(authenticationRequest, authentications, principals, exceptions, messages);

        final int size = authenticationRequest.getCredentials().size();
        if ((authentications.size() != size || principals.size() != size) && this.allCredentialsMustSucceed) {
            return new DefaultAuthenticationResponseImpl(exceptions, messages);
        }

        final AttributePrincipal principal = resolvePrincipal(principals);

        if (principal == null) {
            return new DefaultAuthenticationResponseImpl(exceptions, messages);
        }

        return new DefaultAuthenticationResponseImpl(authentications, principal, exceptions, messages);
    }

    protected abstract void obtainAuthenticationsAndPrincipals(AuthenticationRequest authenticationRequest, Collection<Authentication> authentications, Collection<AttributePrincipal> principals, final Collection<GeneralSecurityException> exceptions, Collection<Message> messages);


    protected final Map<String, List<Object>> obtainAttributesFor(final AuthenticationRequest request, final Credential credential) {
        final Map<String, List<Object>> attributes = new HashMap<String, List<Object>>();
        
        for (final AuthenticationMetaDataResolver resolve : this.authenticationMetaDataResolvers) {
            attributes.putAll(resolve.resolve(request, credential));
        }

        return attributes;
    }

    protected final void obtainMessagesFor(final Credential credential, final AuthenticationHandler authenticationHandler, final Collection<Message> messages) {
        for (final MessageResolver messageResolver : this.messageResolvers) {
            messages.addAll(messageResolver.resolveMessagesFor(credential, authenticationHandler));
        }
    }

    protected final AttributePrincipal resolvePrincipal(final List<AttributePrincipal> successfulPrincipals) {
        AttributePrincipal principal = null;

        for (final AttributePrincipal p : successfulPrincipals) {
            if (principal == null) {
                principal = p;
                continue;
            }

            if (!principal.getName().equals(p.getName())) {
                return null;
            }
        }

        return principal;
    }
}
