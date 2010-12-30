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
import org.jasig.cas.server.session.JpaSessionImpl;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * JPA-compliant implementation of the
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 *
 */
@Entity(name = "authentication")
@Table(name="authentications")
public class JpaAuthenticationImpl extends AbstractAuthenticationImpl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "cas_authentication_seq")
    @SequenceGenerator(name="cas_authentication_seq",sequenceName="cas_authentication_seq",initialValue=1,allocationSize=50)
    private long id;

    @Column(name = "auth_date", nullable = false, insertable = true, updatable = false)
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date authenticationDate = new Date();

    @Column(name = "auth_long_term", nullable = false, insertable = true, updatable = false)
    private boolean longTermAuthentication;

    @Lob
    @Column(name="auth_meta_data")
    private HashMap<String, List<Object>> authenticationMetaData = new HashMap<String, List<Object>>();

    @Column(name = "auth_method", nullable = true, insertable = true, updatable = true)
    private String authenticationMethod;

    @ManyToOne(optional = true, cascade = CascadeType.ALL)
    @JoinColumn(name="session_id", nullable = false)
    private JpaSessionImpl session;

    public JpaAuthenticationImpl() {
        // this should only be called by JPA
    }

    public JpaAuthenticationImpl(final boolean longTermAuthentication, final Map<String, List<Object>> metaData, final String authenticationMethod) {
        this.longTermAuthentication = longTermAuthentication;
        this.authenticationMetaData.putAll(metaData);
        this.authenticationMethod = authenticationMethod;
    }

    public final Date getAuthenticationDate() {
        return new Date(this.authenticationDate.getTime());
    }

    public final Map<String, List<Object>> getAuthenticationMetaData() {
        return this.authenticationMetaData;
    }

    public final boolean isLongTermAuthentication() {
        return this.longTermAuthentication;
    }

    public final String getAuthenticationMethod() {
        return this.authenticationMethod;
    }

    public final void setSession(final JpaSessionImpl session) {
        this.session = session;
    }
}