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

import java.io.Serializable;

/**
 * Warnings from the underlying authentication systems.
 * <p>
 * If a code is provided, it should be used first (the Message should only be used if a code does not exist).
 * <p>
 * Its designed to delegate message resolution to an external party, such as a Spring Message Resolver.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 *
 */
public interface Message extends Serializable {

    /**
     * The code representing this warning.  CAN be null.  But, then, message must NOT be.
     *
     * @return the code representing this warning.
     */
    String getCode();

    /**
     * The message representing this warning.  CAN be null.  But, then, code CANNOT be.
     * @return the custom message, if it wasn't stored in a properties file.
     */
    String getMessage();

    /**
     * Returns the source of the message.  This is a custom field that the deployer can either use for the actual name or
     * some identifying code.
     *
     * @return the source.  CANNOT be NULL.
     */
    String getSource();

    /**
     * The set of arguments to populate the warning with.
     * @return the set of arguments. CANNOT be NULL.  But can be of length 0.
     */
    Object[] getArguments();
}
