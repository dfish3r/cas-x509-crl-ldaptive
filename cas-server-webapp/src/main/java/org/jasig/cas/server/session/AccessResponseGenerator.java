package org.jasig.cas.server.session;

import org.jasig.cas.server.login.ServiceAccessResponse;
import org.springframework.webflow.context.ExternalContext;

import java.io.Writer;

/**
 * Retrieves the {@link org.jasig.cas.server.login.ServiceAccessResponse} from the Access.  Required class because Spring Web Flow
 * can't construct "new" Objects in the flow (as far as I can tell).
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public final class AccessResponseGenerator {

    public AccessResponseResult generateAccessResponseResult(final ExternalContext externalContext, final ServiceAccessResponse serviceAccessResponse) {
        final Writer writer = externalContext.getResponseWriter();
        final AccessResponseRequest accessResponseRequest = new DefaultAccessResponseRequestImpl(writer, null, null);
        return serviceAccessResponse.getAccess().generateResponse(accessResponseRequest);
    }
}
