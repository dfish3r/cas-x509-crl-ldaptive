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

    private static final String DEFAULT_ENCODING = "UTF-8";

    @NotNull
    private final UniqueTicketIdGenerator uniqueTicketIdGenerator;

    @NotNull
    private String encoding = DEFAULT_ENCODING;

    protected AbstractSaml1ProtocolAccessImplFactory(final UniqueTicketIdGenerator uniqueTicketIdGenerator) {
        this.uniqueTicketIdGenerator = uniqueTicketIdGenerator;
    }

    protected UniqueTicketIdGenerator getUniqueTicketIdGenerator() {
        return this.uniqueTicketIdGenerator;
    }

    public final void setEncoding(final String encoding) {
        this.encoding = encoding;
    }

    protected final String getEncoding() {
        return this.encoding;
    }


}
