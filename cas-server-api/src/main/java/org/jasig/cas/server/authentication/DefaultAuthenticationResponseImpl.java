package org.jasig.cas.server.authentication;

import java.security.GeneralSecurityException;
import java.util.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5.0
 */
public final class DefaultAuthenticationResponseImpl implements AuthenticationResponse {
    private final boolean succeeded;

    private final Set<Authentication> authentication;

    private final List<GeneralSecurityException> authenticationExceptions;

    private final List<Message> authenticationMessages;

    private final Map<String, Object> attributes = new HashMap<String, Object>();

    public DefaultAuthenticationResponseImpl() {
        this(new ArrayList<GeneralSecurityException>(), new ArrayList<Message>());
    }

    public DefaultAuthenticationResponseImpl(final List<GeneralSecurityException> authenticationExceptions, final List<Message> authenticationMessages) {
        this(null, authenticationExceptions, authenticationMessages);
    }

    public DefaultAuthenticationResponseImpl(final Set<Authentication> authentication, final List<GeneralSecurityException> authenticationExceptions, final List<Message> authenticationMessages) {
        this.authentication = authentication;
        this.succeeded = this.authentication != null;
        this.authenticationExceptions = Collections.unmodifiableList(authenticationExceptions);
        this.authenticationMessages = Collections.unmodifiableList(authenticationMessages);
    }

    public boolean succeeded() {
        return this.succeeded;
    }

    public Set<Authentication> getAuthentications() {
        return this.authentication;
    }

    public List<GeneralSecurityException> getGeneralSecurityExceptions() {
        return this.authenticationExceptions;
    }

    public List<Message> getAuthenticationMessages() {
        return this.authenticationMessages;
    }

    public Map<String, Object> getAttributes() {
        return this.attributes;
    }
}
