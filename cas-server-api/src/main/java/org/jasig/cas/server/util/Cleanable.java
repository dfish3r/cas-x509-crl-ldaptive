package org.jasig.cas.server.util;

/**
 * Represents a class that can be cleaned by via an external trigger.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface Cleanable {

    /**
     * Indicates that the implementing class has some internal method for pruning itself whenever this method is called.
     * <p>
     * Method is called on-demand and the implementing class should have no internal scheduling mechanism.  If the
     * class has an internal mechanism (i.e. a Timer Thread) then it should not be implementing this interface.  This
     * interface allows for external control of clean up.
     */

    void prune();
}
