package org.jasig.cas.server.session;

import java.util.List;
import java.util.Map;

/**
 * Represents the result of an {@link org.jasig.cas.server.session.Access#generateResponse(AccessResponseRequest)}
 * attempt. Notifies the higher layers whether there is any action they need to take (i.e. REDIRECT, POST, etc.)
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface AccessResponseResult {

    enum Operation {REDIRECT, POST, NONE}

    /**
     * The operation that the higher level should perform based on this result.
     *
     * @return the operation.  Cannot be null.
     */
    Operation getOperationToPerform();

    /**
     * The URL that we should be operating on if the operation is either a REDIRECT or a POST.
     * @return the String.  CAN be null ONLY if the Operation is NONE.
     */
    String getUrl();

    /**
     * The parameters to return if a POST is issued.  For REDIRECT, they should be encoded in the url.
     * @return the parameters map.  CAN be empty but NOT null.
     */
    Map<String, List<String>> getParameters();
}
