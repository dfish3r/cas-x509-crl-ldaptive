package org.jasig.cas.server.session;

import org.jasig.cas.server.login.ServiceAccessRequest;
import org.jasig.cas.server.login.ServiceAccessRequestFactory;

import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: scottbattaglia
 * Date: Jun 15, 2010
 * Time: 11:48:07 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestServiceAccessRequestFactory implements ServiceAccessRequestFactory {

    public ServiceAccessRequest getServiceAccessRequest(String sessionId, String remoteIpAddress, Map parameters) {
        return null;
    }
}
