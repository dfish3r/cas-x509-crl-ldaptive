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

package org.jasig.cas.adaptors.x509.authentication.principal;

import org.jasig.cas.server.authentication.AttributePrincipalFactory;

import javax.validation.constraints.NotNull;
import java.security.cert.X509Certificate;

import java.util.Arrays;
import java.util.Comparator;

/**
 * @author Anders Svensson
 * @author Scott Battaglia
 * @author Barry Silk
 * @version $Revision$ $Date$
 * @since 3.0.4
 */
public final class X509CertificateCredentialsToIdentifierPrincipalResolver extends AbstractX509CertificateCredentialsToPrincipalResolver {

    private static final String DEFAULT_IDENTIFIER = "$OU $CN";

    private static final String ENTRIES_DELIMITER = ",";

    private static final String NAME_VALUE_PAIR_DELIMITER = "=";

    /** The identifier meta data */
    @NotNull
    private String identifier = DEFAULT_IDENTIFIER;

    public X509CertificateCredentialsToIdentifierPrincipalResolver(final AttributePrincipalFactory attributePrincipalFactory) {
        super(attributePrincipalFactory);
    }

    protected String resolvePrincipalInternal(final X509Certificate certificate) {
        String username = this.identifier;
        
        if (log.isInfoEnabled()) {
            log.info("Creating principal for: " + certificate.getSubjectDN().getName());
        }

        final String[] entries = certificate.getSubjectDN().getName().split(ENTRIES_DELIMITER);

        //[fix by Barry Silk]
        // Make sure entries are sorted by length, in descending order
        // This is to prevent a substition of a shorter length descriptor
        // e.g., $CN must get replaced prior to $C
        Arrays.sort(entries, new LengthComparator());
        
        for (final String val : entries) {
            final String[] nameValuePair = val.split(NAME_VALUE_PAIR_DELIMITER);
            final String name = nameValuePair[0].trim();
            final String value = nameValuePair[1];

            if (log.isDebugEnabled()) {
                log.debug("Parsed " + name + " - " + value);
            }

            username = username.replaceAll("\\$" + name, value);
        }
        
        if (this.identifier.equals(username)) {
            return null;
        }

        return username;
    }
    
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }
    
    //[fix by Barry Silk, see above]
    class LengthComparator implements Comparator<String> {
        public int compare(final String s1, final String s2) {
            final String[] nameValuePair1 = s1.split(NAME_VALUE_PAIR_DELIMITER);
            final String name1 = nameValuePair1[0].trim();
            final String[] nameValuePair2 = s2.split(NAME_VALUE_PAIR_DELIMITER);
            final String name2 = nameValuePair2[0].trim();
            final int len1 = name1.length();
            final int len2 = name2.length();
            if (len1 > len2) {
                return -1;
            }
            if (len2 > len1) {
                return 1;
            }
            return 0;
        }
    }

}
