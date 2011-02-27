/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.jasig.cas.server.login;

import org.jasig.cas.server.authentication.AuthenticationResponse;
import org.jasig.cas.server.session.Access;
import org.jasig.cas.server.session.ServiceAccessResponseFactory;
import org.jasig.cas.server.session.Session;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: battags
 * Date: 2/26/11
 * Time: 9:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class Saml11RequestAccessResponseImplFactory implements ServiceAccessResponseFactory {

    public ServiceAccessResponse getServiceAccessResponse(ServiceAccessRequest serviceAccessRequest) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ServiceAccessResponse getServiceAccessResponse(ServiceAccessRequest serviceAccessRequest, AuthenticationResponse authenticationResponse) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public ServiceAccessResponse getServiceAccessResponse(Session session, Access access, AuthenticationResponse authenticationResponse, List<Access> loggedOutAccesses) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean supports(ServiceAccessRequest serviceAccessRequest) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    public boolean supports(Access access) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }
}
