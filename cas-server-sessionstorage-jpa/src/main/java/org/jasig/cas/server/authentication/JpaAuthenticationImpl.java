package org.jasig.cas.server.authentication;

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
@Embeddable()
public final class JpaAuthenticationImpl implements Authentication {

    @Column(name = "auth_date", nullable = false, insertable = true, updatable = false)
    @Temporal(javax.persistence.TemporalType.TIME)
    private Date authenticationDate = new Date();

    @Column(name = "auth_long_term", nullable = false, insertable = true, updatable = false)
    private boolean longTermAuthentication;

    @Embedded
    private JpaAttributePrincipalImpl attributePrincipal;

    @Lob
    @Column(name="auth_meta_data")
    private HashMap<String, List<Object>> authenticationMetaData = new HashMap<String, List<Object>>();

    public JpaAuthenticationImpl() {
        // this should only be called by JPA
    }

    public JpaAuthenticationImpl(final AttributePrincipal attributePrincipal, final boolean longTermAuthentication, final Map<String, List<Object>> metaData) {
        Assert.isInstanceOf(JpaAttributePrincipalImpl.class, attributePrincipal);
        this.longTermAuthentication = longTermAuthentication;
        this.attributePrincipal = (JpaAttributePrincipalImpl) attributePrincipal;
        this.authenticationMetaData.putAll(metaData);
    }

    public Date getAuthenticationDate() {
        return new Date(this.authenticationDate.getTime());
    }

    public Map<String, List<Object>> getAuthenticationMetaData() {
        return this.authenticationMetaData;
    }

    public AttributePrincipal getPrincipal() {
        return this.attributePrincipal;
    }

    public boolean isLongTermAuthentication() {
        return this.longTermAuthentication;
    }
}