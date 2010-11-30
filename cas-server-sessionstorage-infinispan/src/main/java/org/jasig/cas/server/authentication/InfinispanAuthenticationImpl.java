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

package org.jasig.cas.server.authentication;

import org.jasig.cas.server.session.AbstractAuthenticationImpl;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Immutable version of the {@link org.jasig.cas.server.authentication.Authentication} interface that can easily
 * be stored via JBoss Infinispan.
 *
 * @author Scott Battaglia
 * @version $Revision: 22274 $ $Date: 2010-11-08 00:10:58 -0500 (Mon, 08 Nov 2010) $
 * @since 3.5
 */
public final class InfinispanAuthenticationImpl extends AbstractAuthenticationImpl {

    private final Date authenticationDate = new Date();

    private final boolean longTermAuthentication;

    private final String authenticationMethod;

    private final Map<String, List<Object>> authenticationMetaData;

    public InfinispanAuthenticationImpl(final Map<String, List<Object>> authenticationMetaData, final boolean longTermAuthentication, final String authenticationMethod) {
        this.authenticationMetaData = Collections.unmodifiableMap(authenticationMetaData);
        this.longTermAuthentication = longTermAuthentication;
        this.authenticationMethod = authenticationMethod;
    }

    public Date getAuthenticationDate() {
        return new Date(authenticationDate.getTime());
    }

    public Map<String, List<Object>> getAuthenticationMetaData() {
        return this.authenticationMetaData;
    }

    public boolean isLongTermAuthentication() {
        return this.longTermAuthentication;
    }

    public String getAuthenticationMethod() {
        return this.authenticationMethod;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final InfinispanAuthenticationImpl that = (InfinispanAuthenticationImpl) o;

        if (longTermAuthentication != that.longTermAuthentication) return false;
        if (authenticationMetaData != null ? !authenticationMetaData.equals(that.authenticationMetaData) : that.authenticationMetaData != null)
            return false;
        if (authenticationMethod != null ? !authenticationMethod.equals(that.authenticationMethod) : that.authenticationMethod != null)
            return false;
//        if (date != null ? !date.equals(that.date) : that.date != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = authenticationDate != null ? authenticationDate.hashCode() : 0;
        result = 31 * result + (longTermAuthentication ? 1 : 0);
        result = 31 * result + (authenticationMethod != null ? authenticationMethod.hashCode() : 0);
        return result;
    }
}
