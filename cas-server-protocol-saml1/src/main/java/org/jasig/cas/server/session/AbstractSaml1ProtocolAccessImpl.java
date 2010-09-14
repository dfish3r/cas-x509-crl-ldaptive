package org.jasig.cas.server.session;

import org.jasig.cas.server.login.Saml1TokenServiceAccessRequestImpl;
import org.jasig.cas.server.login.TokenServiceAccessRequest;
import org.springframework.util.Assert;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSaml1ProtocolAccessImpl implements Access {

    protected enum ValidationStatus {NOT_VALIDATED, VALIDATED, USED, ALREADY_VALIDATED};

    protected abstract ValidationStatus getValidationStatus();

    protected abstract void setValidationStatus(ValidationStatus validationStatus);

    public final boolean requiresStorage() {
        return true;
    }

    public final boolean isLocalSessionDestroyed() {
        return false;
    }

    public final synchronized void validate(final TokenServiceAccessRequest tokenServiceAccessRequest) {
        final ValidationStatus validationStatus = getValidationStatus();
        Assert.isInstanceOf(Saml1TokenServiceAccessRequestImpl.class, tokenServiceAccessRequest, "Invalid token validation request");

        final Saml1TokenServiceAccessRequestImpl saml1TokenServiceAccessRequest = (Saml1TokenServiceAccessRequestImpl) tokenServiceAccessRequest;

        if (validationStatus != ValidationStatus.NOT_VALIDATED) {
            setValidationStatus(ValidationStatus.ALREADY_VALIDATED);
            return;
        }

    }
}
