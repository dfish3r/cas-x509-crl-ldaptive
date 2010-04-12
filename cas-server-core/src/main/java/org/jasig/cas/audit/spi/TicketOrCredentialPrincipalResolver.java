/*
 * Copyright 2007 The JA-SIG Collaborative. All rights reserved. See license
 * distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.audit.spi;

import org.aspectj.lang.JoinPoint;
import com.github.inspektr.common.spi.PrincipalResolver;
import org.jasig.cas.server.authentication.Credential;
import org.jasig.cas.server.session.Session;
import org.jasig.cas.server.session.SessionStorage;
import org.jasig.cas.util.AopUtils;

import javax.validation.constraints.NotNull;

/**
 * PrincipalResolver that can retrieve the username from either the Ticket or from the Credentials.
 * 
 * @author Scott Battaglia
 * @version $Revision: 1.1 $ $Date: 2005/08/19 18:27:17 $
 * @since 3.1.2
 *
 */
public final class TicketOrCredentialPrincipalResolver implements PrincipalResolver {
    
    @NotNull
    private final SessionStorage sessionStorage;

    public TicketOrCredentialPrincipalResolver(final SessionStorage sessionStorage) {
        this.sessionStorage = sessionStorage;
    }

    public String resolveFrom(final JoinPoint joinPoint, final Object retVal) {
        return resolveFromInternal(AopUtils.unWrapJoinPoint(joinPoint));
    }

    public String resolveFrom(final JoinPoint joinPoint, final Exception retVal) {
        return resolveFromInternal(AopUtils.unWrapJoinPoint(joinPoint));
    }

    public String resolve() {
        return UNKNOWN_USER;
    }
    
    protected String resolveFromInternal(final JoinPoint joinPoint) {
        final Object arg1 = joinPoint.getArgs()[0];
        if (arg1 instanceof Credential) {
           return arg1.toString();
        } else if (arg1 instanceof String) {
            final String id = (String) arg1;
            final Session session = this.sessionStorage.findSessionBySessionId(id);
            final Session foundSession;

            if (session != null) {
                foundSession = session;
            } else {
                foundSession = this.sessionStorage.findSessionByAccessId(id);
            }

            if (foundSession != null) {
                return foundSession.getRootAuthentications().iterator().next().getPrincipal().getName();
            }

        }
        return UNKNOWN_USER;
    }
}
