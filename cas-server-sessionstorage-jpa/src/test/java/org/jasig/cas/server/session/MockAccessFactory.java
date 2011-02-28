package org.jasig.cas.server.session;

import org.jasig.cas.server.login.ServiceAccessRequest;
import org.jasig.cas.server.util.DefaultServiceIdentifierMatcherImpl;

import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 */
public class MockAccessFactory extends AbstractCasProtocolAccessImplFactory {

    public Access getAccess(final Session session, final ServiceAccessRequest serviceAccessRequest) {
        AbstractStaticCasProtocolAccessImpl.setExpirationPolicy(getExpirationPolicy());
        AbstractStaticCasProtocolAccessImpl.setProxyHandler(getProxyHandler());
        AbstractStaticCasProtocolAccessImpl.setServiceIdentifierMatcher(getServiceIdentifierMatcher());
        AbstractStaticCasProtocolAccessImpl.setUniqueTicketIdGenerator(getUniqueTicketIdGenerator());
        return new JpaCasProtocolAccessImpl(session, serviceAccessRequest);
    }
}
