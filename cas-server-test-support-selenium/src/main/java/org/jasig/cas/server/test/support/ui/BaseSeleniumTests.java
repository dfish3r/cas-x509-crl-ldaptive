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

package org.jasig.cas.server.test.support.ui;

import org.jasig.cas.server.test.support.ui.api.Page;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.openqa.selenium.WebDriver;


/**
 * Base class for future UI Tests.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 *
 */
public abstract class BaseSeleniumTests {
	
	protected WebDriver driver;
	
	@BeforeClass
	public void beforeClass() throws Exception {
		this.driver = createDriver();
	}
	
	public final boolean at(final Page page) {
		return page.at();	
	}

    @AfterClass
	public void afterClass() throws Exception {
		this.driver.quit();
	}
	
	public void to(final Page page) {
		this.driver.navigate().to(constructUrl(page));
	}
	
	@After
	public void afterMethod() throws Exception {
		this.driver.manage().deleteAllCookies();
	}
	
	protected String getBaseUrl() {
		final String driverUrl = this.driver.getCurrentUrl();
		
		if (driverUrl == null) {
			return "";
		}
		
		final int indexOf = driverUrl.indexOf("/", "http://".length());
		
		if (indexOf == -1) {
			return driverUrl;
		}
		
		return driverUrl.substring(0, indexOf - 1);
	}
	
	private String constructUrl(final Page page) {
		if (page.url() == null) {
			throw new IllegalStateException();
		}
		
		final String lowercasePage = page.url().toLowerCase();
		
		if (lowercasePage.startsWith("http://") || lowercasePage.startsWith("https://")) {
			return page.url();
		}
		
		return getBaseUrl() + page.url();
	}
	
	protected WebDriver createDriver() {
		final Class<? extends WebDriver> firstAttempt = findAvailableDriverClass("org.openqa.selenium.firefox.FirefoxDriver");
		
		if (firstAttempt != null) {
            return createInstance(firstAttempt);
		}

    	final Class<? extends WebDriver> secondAttempt = findAvailableDriverClass("org.openqa.selenium.chrome.ChromeDriver");
			
        if (secondAttempt != null) {
            return createInstance(secondAttempt);
        }

		final Class<? extends WebDriver> thirdAttempt = findAvailableDriverClass("org.openqa.selenium.ie.InternetExplorerDriver");
				
        if (thirdAttempt != null) {
            return createInstance(thirdAttempt);
        }

        final Class<? extends WebDriver> fourthAttempt = findAvailableDriverClass("org.openqa.selenium.htmlunit.HtmlUnitDriver");

        if (fourthAttempt != null) {
            return createInstance(fourthAttempt);
		}

		throw new IllegalStateException("No Selenium WebDriver Found.  Do you have one on the classpath?");
	}
	
	private WebDriver createInstance(Class<? extends WebDriver> clazz) {
		try {
			return clazz.newInstance();
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	private Class<? extends WebDriver> findAvailableDriverClass(final String className) {
		try {
			return (Class<? extends WebDriver>) Class.forName(className);
		} catch (final ClassNotFoundException e) {
			return null;
		}
	}
		

}
