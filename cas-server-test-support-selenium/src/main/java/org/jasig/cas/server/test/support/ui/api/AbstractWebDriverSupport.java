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

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ByIdOrName;

/**
 * Provides a slightly simpler API for using the findElement aspects of the WebDriver.
 * 
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 4.0.0
 *
 */
public abstract class AbstractWebDriverSupport {
	
	private final WebDriver webDriver;
	
	protected AbstractWebDriverSupport(final WebDriver webDriver) {
		this.webDriver = webDriver;
	}
	
	protected final WebDriver getWebDriver() {
		return this.webDriver;
	}
	
	protected final WebElement findElementByXpath(final String xpath) {
		return getWebDriver().findElement(By.xpath(xpath));
	}
	
	protected final List<WebElement> findElementsByXpath(final String xpath) {
		return getWebDriver().findElements(By.xpath(xpath));
	}
	
	protected final WebElement findElementByCssSelector(final String cssSelector) {
		return getWebDriver().findElement(By.cssSelector(cssSelector));
	}
	
	protected final List<WebElement> findElementsByCssSelector(final String cssSelector) {
		return getWebDriver().findElements(By.cssSelector(cssSelector));
	}
	
	protected final WebElement findElementByClassName(final String className) {
		return getWebDriver().findElement(By.className(className));
	}
	
	protected final List<WebElement> findElementsByClassName(final String className) {
		return getWebDriver().findElements(By.className(className));
	}
	
	protected final WebElement findElementById(final String id) {
		return getWebDriver().findElement(By.id(id));
	}
	
	protected final WebElement findElementByName(final String name) {
		return getWebDriver().findElement(By.name(name));
	}

	protected final List<WebElement> findElementsByName(final String name) {
		return getWebDriver().findElements(By.name(name));
	}
	
	protected final WebElement findElementByLinkText(final String linkText) {
		return getWebDriver().findElement(By.linkText(linkText));
	}
	
	protected final List<WebElement> findElementsByLinkText(final String linkText) {
		return getWebDriver().findElements(By.linkText(linkText));
	}
	
	protected final List<WebElement> findElementsByPartialLinkText(final String partialLinkText) {
		return getWebDriver().findElements(By.partialLinkText(partialLinkText));
	}
	
	protected final WebElement findElementByPartialLinkText(final String partialLinkText) {
		return getWebDriver().findElement(By.partialLinkText(partialLinkText));
	}
	
	protected final WebElement findElementByTagName(final String name) {
		return getWebDriver().findElement(By.tagName(name));
	}
	
	protected final List<WebElement> findElementsByTagName(final String name) {
		return getWebDriver().findElements(By.tagName(name));
	}
	
	protected final List<WebElement> findElementsByIdOrName(final String idOrName) {
		return new ByIdOrName(idOrName).findElements(getWebDriver());
	}

	protected final WebElement findElementByIdOrName(final String idOrName) {
		return new ByIdOrName(idOrName).findElement(getWebDriver());
	}
}
