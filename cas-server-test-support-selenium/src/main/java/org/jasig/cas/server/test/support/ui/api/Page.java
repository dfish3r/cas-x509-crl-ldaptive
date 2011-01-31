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

package org.jasig.cas.server.test.support.ui.api;

import org.openqa.selenium.WebDriver;

public abstract class Page extends AbstractWebDriverSupport {
	
	private final Matcher atMatcher;
	
	private final String url;
	
	protected Page(final WebDriver webDriver, final String url, final Matcher matcher) {
		super(webDriver);
		this.url = url;
		this.atMatcher = matcher;
	}
	
	public final String url() {
		return this.url;
	}
	
	public boolean at() {
		return this.atMatcher.matches();
	}
}
