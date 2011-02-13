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

package org.jasig.cas.adaptors.generic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.commons.io.IOUtils;
import org.jasig.cas.server.authentication.AbstractUsernamePasswordAuthenticationHandler;
import org.jasig.cas.server.authentication.UserNamePasswordCredential;
import org.springframework.core.io.Resource;

import javax.validation.constraints.NotNull;

/**
 * Class designed to read data from a file in the format of USERNAME SEPARATOR
 * PASSWORD that will go line by line and look for the username. If it finds the
 * username it will compare the supplied password (first put through a
 * PasswordTranslator) that is compared to the password provided in the file. If
 * there is a match, the user is authenticated. Note that the default password
 * translator is a plaintext password translator and the default separator is
 * "::" (without quotes).
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.0
 */
public final class FileAuthenticationHandler extends AbstractUsernamePasswordAuthenticationHandler {

    /** The default separator in the file. */
    private static final String DEFAULT_SEPARATOR = "::";

    /** The separator to use. */
    @NotNull
    private String separator = DEFAULT_SEPARATOR;

    /** The filename to read the list of usernames from. */
    @NotNull
    private Resource fileName;

    protected boolean authenticateUsernamePasswordInternal(final UserNamePasswordCredential credentials) {
        BufferedReader bufferedReader = null;

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(this.fileName.getInputStream()));
            String line = bufferedReader.readLine();

            while (line != null) {
                if (!line.contains(this.separator)) {
                    line = bufferedReader.readLine();
                    continue;
                }

                final String[] lineFields = line.split(this.separator);

                if (lineFields.length != 2) {
                    line = bufferedReader.readLine();
                    continue;
                }

                final String userName = lineFields[0];
                final String password = lineFields[1];

                final String transformedUsername = getPrincipalNameTransformer().transform(credentials.getUserName());
                if (transformedUsername.equals(userName)) {
                    if (this.getPasswordEncoder().isValidPassword(password, credentials.getPassword(), null)) {
                        return true;
                    }
                    break;
                }
                line = bufferedReader.readLine();
            }
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            IOUtils.closeQuietly(bufferedReader);
        }

        return false;
    }

    /**
     * @param fileName The fileName to set.
     */
    public void setFileName(final Resource fileName) {
        this.fileName = fileName;
    }

    /**
     * @param separator The separator to set.
     */
    public void setSeparator(final String separator) {
        this.separator = separator;
    }
}
