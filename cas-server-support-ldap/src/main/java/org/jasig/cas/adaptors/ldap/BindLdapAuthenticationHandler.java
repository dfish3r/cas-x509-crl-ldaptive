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

package org.jasig.cas.adaptors.ldap;

import org.jasig.cas.server.authentication.GeneralSecurityExceptionTranslator;
import org.jasig.cas.server.authentication.UserNamePasswordCredential;
import org.jasig.cas.util.LdapUtils;
import org.springframework.ldap.AuthenticationException;
import org.springframework.ldap.core.ContextSource;
import org.springframework.ldap.core.NameClassPairCallbackHandler;
import org.springframework.ldap.core.SearchExecutor;

import javax.inject.Inject;
import javax.naming.NameClassPair;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.List;

/**
 * Handler to do LDAP bind.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0.3
 */
public class BindLdapAuthenticationHandler extends AbstractLdapUsernamePasswordAuthenticationHandler {

    /** The default maximum number of results to return. */
    private static final int DEFAULT_MAX_NUMBER_OF_RESULTS = 1000;

    /** The default timeout. */
    private static final int DEFAULT_TIMEOUT = 1000;

    /** The search base to find the user under. */
    private String searchBase;

    /** The scope. */
    @Min(0)
    @Max(2)
    private int scope = SearchControls.SUBTREE_SCOPE;

    /** The maximum number of results to return. */
    private int maxNumberResults = DEFAULT_MAX_NUMBER_OF_RESULTS;

    /** The amount of time to wait. */
    private int timeout = DEFAULT_TIMEOUT;

    /** Boolean of whether multiple accounts are allowed. */
    private boolean allowMultipleAccounts;

    @Inject
    public BindLdapAuthenticationHandler(final ContextSource contextSource, final GeneralSecurityExceptionTranslator generalSecurityExceptionTranslator) {
        super(contextSource, generalSecurityExceptionTranslator);
    }

    protected final boolean authenticateUsernamePasswordInternal(final UserNamePasswordCredential credentials) throws GeneralSecurityException {

        final List<String> cns = new ArrayList<String>();
        
        final SearchControls searchControls = getSearchControls();
        
        final String base = this.searchBase;
        final String transformedUsername = getPrincipalNameTransformer().transform(credentials.getUserName());
        final String filter = LdapUtils.getFilterWithValues(getFilter(), transformedUsername);
        this.getLdapTemplate().search(
            new SearchExecutor() {

                public NamingEnumeration executeSearch(final DirContext context) throws NamingException {
                    return context.search(base, filter, searchControls);
                }
            },
            new NameClassPairCallbackHandler(){

                public void handleNameClassPair(final NameClassPair nameClassPair) {
                    cns.add(nameClassPair.getNameInNamespace());
                }
            });
        
        if (cns.isEmpty()) {
            log.info("Search for " + filter + " returned 0 results.");
            return false;
        }
        if (cns.size() > 1 && !this.allowMultipleAccounts) {
            log.warn("Search for " + filter + " returned multiple results, which is not allowed.");
            return false;
        }

        org.springframework.ldap.NamingException namingException = null;
        for (final String dn : cns) {
            DirContext test = null;
            String finalDn = composeCompleteDnToCheck(dn, credentials);
            try {
                namingException = null;
                this.log.debug("Performing LDAP bind with credential: " + dn);
                test = this.getContextSource().getContext(
                    finalDn,
                    credentials.getPassword());

                if (test != null) {
                    return true;
                }
            } catch (final org.springframework.ldap.NamingException e) {
                namingException = e;
                // if we catch an exception, just try the next cn
            } finally {
                LdapUtils.closeContext(test);
            }
        }

        if (namingException != null && namingException instanceof AuthenticationException) {
            throw getGeneralSecurityExceptionTranslator().translateExceptionIfPossible((AuthenticationException) namingException);
        }

        return false;
    }

    protected String composeCompleteDnToCheck(final String dn, final UserNamePasswordCredential credentials) {
        return dn;
    }

    private SearchControls getSearchControls() {
        final SearchControls constraints = new SearchControls();
        constraints.setSearchScope(this.scope);
        constraints.setReturningAttributes(new String[0]);
        constraints.setTimeLimit(this.timeout);
        constraints.setCountLimit(this.maxNumberResults);

        return constraints;
    }

    /**
     * Method to return whether multiple accounts are allowed.
     * @return true if multiple accounts are allowed, false otherwise.
     */
    protected boolean isAllowMultipleAccounts() {
        return this.allowMultipleAccounts;
    }

    /**
     * Method to return the max number of results allowed.
     * @return the maximum number of results.
     */
    protected int getMaxNumberResults() {
        return this.maxNumberResults;
    }

    /**
     * Method to return the scope.
     * @return the scope
     */
    protected int getScope() {
        return this.scope;
    }

    /**
     * Method to return the search base.
     * @return the search base.
     */
    protected String getSearchBase() {
        return this.searchBase;
    }

    /**
     * Method to return the timeout. 
     * @return the timeout.
     */
    protected int getTimeout() {
        return this.timeout;
    }

    public final void setScope(final int scope) {
        this.scope = scope;
    }

    /**
     * @param allowMultipleAccounts The allowMultipleAccounts to set.
     */
    public void setAllowMultipleAccounts(final boolean allowMultipleAccounts) {
        this.allowMultipleAccounts = allowMultipleAccounts;
    }

    /**
     * @param maxNumberResults The maxNumberResults to set.
     */
    public final void setMaxNumberResults(final int maxNumberResults) {
        this.maxNumberResults = maxNumberResults;
    }

    /**
     * @param searchBase The searchBase to set.
     */
    public final void setSearchBase(final String searchBase) {
        this.searchBase = searchBase;
    }

    /**
     * @param timeout The timeout to set.
     */
    public final void setTimeout(final int timeout) {
        this.timeout = timeout;
    }
}