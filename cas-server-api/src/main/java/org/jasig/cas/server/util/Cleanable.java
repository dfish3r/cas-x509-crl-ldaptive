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

/**
 * Represents a class that can be cleaned by via an external trigger.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface Cleanable {

    /**
     * Indicates that the implementing class has some internal method for pruning itself whenever this method is called.
     * <p>
     * Method is called on-demand and the implementing class should have no internal scheduling mechanism.  If the
     * class has an internal mechanism (i.e. a Timer Thread) then it should not be implementing this interface.  This
     * interface allows for external control of clean up.
     */

    void prune();
}
