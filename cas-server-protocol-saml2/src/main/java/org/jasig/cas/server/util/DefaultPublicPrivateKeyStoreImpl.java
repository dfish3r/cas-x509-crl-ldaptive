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

package org.jasig.cas.server.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.Certificate;

/**
 * Manages a set of keys via the standard Java method for storing keys.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class DefaultPublicPrivateKeyStoreImpl implements PublicPrivateKeyStore {

    private final Logger log = LoggerFactory.getLogger(getClass());

    @NotNull
    private final KeyStore publicKeyStore;

    @NotNull
    private final KeyStore privateKeyStore;

    @NotNull
    private final String privateKeyStorePassword;

    public DefaultPublicPrivateKeyStoreImpl(final Resource publicKeyStoreLocation, final String publicKeyStorePassword, final Resource privateKeyStoreLocation, final String privateKeyStorePassword) throws Exception {
        this.publicKeyStore = loadKeyStore(publicKeyStoreLocation, publicKeyStorePassword);
        this.privateKeyStore = loadKeyStore(privateKeyStoreLocation, privateKeyStorePassword);
        this.privateKeyStorePassword = privateKeyStorePassword;
    }

    protected KeyStore loadKeyStore(final Resource resource, final String password) throws Exception {
        final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        final InputStream stream = resource.getInputStream();
        keyStore.load(stream, password.toCharArray());

        return keyStore;
    }

    public PublicKey getPublicKey(final String alias) {
        try {
            final Certificate cert = this.publicKeyStore.getCertificate(alias);
            return 	cert.getPublicKey();
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    public PrivateKey getPrivateKey(final String alias) {
        try {
            return (PrivateKey) this.privateKeyStore.getKey(alias, this.privateKeyStorePassword.toCharArray());
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }
}
