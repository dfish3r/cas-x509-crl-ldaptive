package org.jasig.cas.server.authentication;

import java.io.Serializable;

/**
 * Warnings from the underlying authentication systems.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 *
 */
public interface Message extends Serializable {

    /**
     * The code representing this warning.  CAN be null.  But, then, message must NOT be.
     *
     * @return the code representing this warning.
     */
    String getCode();

    /**
     * The message representing this warning.  CAN be null.  But, then, code CANNOT be.
     * @return the custom message, if it wasn't stored in a properties file.
     */
    String getMessage();

    /**
     * The set of arguments to populate the warning with.
     * @return the set of arguments. CANNOT be NULL.  But can be of length 0.
     */
    Object[] getArguments();
}
