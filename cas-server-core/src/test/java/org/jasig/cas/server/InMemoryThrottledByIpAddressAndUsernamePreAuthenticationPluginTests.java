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

package org.jasig.cas.server;

/**
 * Test Cases for {@link org.jasig.cas.server.InMemoryThrottledByIpAddressAndUsernamePreAuthenticationPlugin}
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public final class InMemoryThrottledByIpAddressAndUsernamePreAuthenticationPluginTests extends AbstractThrottledPreAuthenticationPluginTests {

    @Override
    protected AbstractThrottlingPreAuthenticationPlugin getPreAuthenticationPlugin() {
        final InMemoryThrottledByIpAddressAndUsernamePreAuthenticationPlugin plugin = new InMemoryThrottledByIpAddressAndUsernamePreAuthenticationPlugin();
        plugin.setFailureRangeInSeconds(CONST_FAILURE_TIMEOUT);
        plugin.setFailureThreshold(CONST_FAILURE_THRESHHOLD);
        return plugin;
    }

    @Override
    protected void decrementCount(final AbstractThrottlingPreAuthenticationPlugin plugin) {
        ((InMemoryThrottledByIpAddressAndUsernamePreAuthenticationPlugin) plugin).decrementCounts();
    }
}
