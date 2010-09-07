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

package org.jasig.cas;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import org.jasig.cas.server.authentication.*;
import org.jasig.cas.server.session.AbstractAuthenticationImpl;
import org.jasig.cas.server.session.Assertion;
import static org.mockito.Mockito.*;

/**
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.2
 */
public final class TestUtils {

    public static final String CONST_USERNAME = "test";

    private static final String CONST_PASSWORD = "test1";

    private static final String CONST_BAD_URL = "http://www.acs.rutgers.edu";

    private static final String CONST_CREDENTIALS = "credentials";

    private static final String CONST_WEBFLOW_BIND_EXCEPTION = "org.springframework.validation.BindException.credentials";

    private static final String[] CONST_NO_PRINCIPALS = new String[0];

    public static final String CONST_EXCEPTION_EXPECTED = "Exception expected.";

    public static final String CONST_EXCEPTION_NON_EXPECTED = "Exception not expected.";
    
    public static final String CONST_GOOD_URL = "https://wwws.mint.com/";

    private TestUtils() {
        // do not instanciate
    }

    public static AuthenticationRequest getAuthenticationRequest(final Credential credential) {
        final AuthenticationRequest authenticationRequest = mock(AuthenticationRequest.class);
        when(authenticationRequest.getAuthenticationRequestDate()).thenReturn(new Date());
        when(authenticationRequest.isLongTermAuthenticationRequest()).thenReturn(false);
        when(authenticationRequest.getCredentials()).thenReturn(Arrays.asList(credential));
        return authenticationRequest;
    }

    public static UserNamePasswordCredential getCredentialsWithSameUsernameAndPassword() {
        return getCredentialsWithSameUsernameAndPassword(CONST_USERNAME);
    }

    public static UserNamePasswordCredential getCredentialsWithSameUsernameAndPassword(
        final String username) {
        return getCredentialsWithDifferentUsernameAndPassword(username,
            username);
    }

    public static UserNamePasswordCredential getCredentialsWithDifferentUsernameAndPassword() {
        return getCredentialsWithDifferentUsernameAndPassword(CONST_USERNAME,
            CONST_PASSWORD);
    }

    public static UserNamePasswordCredential getCredentialsWithDifferentUsernameAndPassword(
        final String username, final String password) {

        return new UserNamePasswordCredential() {
            public String getUserName() {
                return username;
            }

            public String getPassword() {
                return password;
            }

            @Override
            public boolean equals(final Object obj) {
                if (obj == null) {
                    return false;
                }

                if (!(obj instanceof UserNamePasswordCredential)) {
                    return false;
                }

                final UserNamePasswordCredential c = (UserNamePasswordCredential) obj;

                return getUserName().equals(c.getUserName()) && getPassword().equals(c.getPassword());
            }

            @Override
            public int hashCode() {
                int result = username != null ? username.hashCode() : 0;
                result = 31 * result + (password != null ? password.hashCode() : 0);
                return result;
            }
        };
    }

    public static UrlCredential getHttpBasedServiceCredentials() {
        return getHttpBasedServiceCredentials(CONST_GOOD_URL);
    }

    public static UrlCredential getBadHttpBasedServiceCredentials() {
        return getHttpBasedServiceCredentials(CONST_BAD_URL);
    }

    public static UrlCredential getHttpBasedServiceCredentials(final String url) {
        try {
            final URL actualUrl = new URL(url);
            return new UrlCredential() {
                public URL getUrl() {
                    return actualUrl;
                }
            };
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException();
        }
    }

    public static AttributePrincipal getPrincipal() {
        return getPrincipal(CONST_USERNAME);
    }

    public static AttributePrincipal getPrincipal(final String name) {
        return new AttributePrincipal() {

            public List<Object> getAttributeValues(final String attribute) {
                return null;
            }

            public Object getAttributeValue(final String attribute) {
                return null;
            }

            public Map<String, List<Object>> getAttributes() {
                final Map<String, List<Object>> values = new HashMap<String, List<Object>>();
                values.put("testAttribute", Arrays.asList((Object) "testValue"));
                values.put("testAttributeCollection", Arrays.asList((Object) "tac1", "tac2"));
                return values;
            }

            public String getName() {
                return name;
            }
        };
    }

    public static Service getService() {
        return getService(CONST_USERNAME);
    }

    public static Service getService(final String name) {
        return new Service() {
            public String getId() {
                return name;
            }

            public void setPrincipal(AttributePrincipal principal) {

            }

            public boolean logOutOfService(String sessionIdentifier) {
                return false;
            }

            public boolean matches(Service service) {
                return name.equals(service.getId());
            }

            public boolean equals(final Object o) {
                if (o == null || !(o instanceof Service)) {
                    return false;
                }

                final Service s = (Service) o;
                return s.getId().equals(this.getId()); 
            }
        };
    }

    public static Authentication getAuthentication() {

        return new AbstractAuthenticationImpl() {

            private final Date date = new Date();


            public Date getAuthenticationDate() {
                return date;
            }

            public Map<String, List<Object>> getAuthenticationMetaData() {
                return Collections.emptyMap();
            }

            public boolean isLongTermAuthentication() {
                return false;
            }

            public String getAuthenticationMethod() {
                return "urn:ietf:rfc:2246";
            }
        };
    }

    public static Authentication getAuthentication(final String name) {
        final AttributePrincipal attributePrincipal = getPrincipal(name);

        return new AbstractAuthenticationImpl() {

            private final Date date = new Date();


            public Date getAuthenticationDate() {
                return date;
            }

            public Map<String, List<Object>> getAuthenticationMetaData() {
                return Collections.emptyMap();
            }

            public AttributePrincipal getPrincipal() {
                return attributePrincipal;
            }

            public boolean isLongTermAuthentication() {
                return false;
            }

            public String getAuthenticationMethod() {
                return "urn:ietf:rfc:2246";
            }
        };
    }

    public static Assertion getAssertion(final boolean fromNewLogin) {
        return getAssertion(fromNewLogin, CONST_NO_PRINCIPALS);
    }

    public static Assertion getAssertion(final boolean fromNewLogin,
        final String[] extraPrincipals) {
        final List<Authentication> list = new ArrayList<Authentication>();
        list.add(TestUtils.getAuthentication());

        for (final String extraPrincipal : extraPrincipals) {
            list.add(TestUtils.getAuthentication(extraPrincipal));
        }

        return new Assertion() {
            public List<Authentication> getChainedAuthentications() {
                return list;
            }

            public AttributePrincipal getPrincipal() {
                return TestUtils.getPrincipal();
            }

            public boolean isFromNewLogin() {
                return fromNewLogin;
            }

            public Service getService() {
                return TestUtils.getService();
            }
        };
    }

    /*
    public static MockRequestContext getContext() {
        return getContext(new MockHttpServletRequest());
    }

    public static MockRequestContext getContext(
        final MockHttpServletRequest request) {
        return getContext(request, new MockHttpServletResponse());
    }

    public static MockRequestContext getContext(
        final MockHttpServletRequest request,
        final MockHttpServletResponse response) {
        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(new MockServletContext(), request, response));
        return context;
    }

    public static MockRequestContext getContextWithCredentials(
        final MockHttpServletRequest request) {
        return getContextWithCredentials(request, new MockHttpServletResponse());
    }

    public static MockRequestContext getContextWithCredentials(
        final MockHttpServletRequest request,
        final MockHttpServletResponse response) {
        final MockRequestContext context = getContext(request, response);
        context.getRequestScope().put(CONST_CREDENTIALS, TestUtils
            .getCredentialsWithSameUsernameAndPassword());
        context.getRequestScope().put(CONST_WEBFLOW_BIND_EXCEPTION,
                new BindException(TestUtils
                    .getCredentialsWithSameUsernameAndPassword(),
                    CONST_CREDENTIALS));

        return context;
    } */
}
