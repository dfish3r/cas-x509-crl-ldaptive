package org.jasig.cas.server.session;

import org.jasig.cas.server.util.UniqueTicketIdGenerator;

import javax.validation.constraints.NotNull;

/**
 * Abstract SAMl1 access factory that provides some required attributes.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public abstract class AbstractSaml1ProtocolAccessImplFactory implements AccessFactory {

    @NotNull
    private final UniqueTicketIdGenerator uniqueTicketIdGenerator;

    protected AbstractSaml1ProtocolAccessImplFactory(final UniqueTicketIdGenerator uniqueTicketIdGenerator) {
        this.uniqueTicketIdGenerator = uniqueTicketIdGenerator;
    }

    protected UniqueTicketIdGenerator getUniqueTicketIdGenerator() {
        return this.uniqueTicketIdGenerator;
    }
}
