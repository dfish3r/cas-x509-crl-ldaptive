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

import java.util.Date;

/**
 * Parses a date from a String.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public interface DateParser {

    /**
     * Parses the given String into the required date.  Provided Strings should be appropriately matched to correct
     * DateParser.
     *
     * @param dateAsString the Date as a String. CANNOT be NULL.
     *
     * @return the constructed date, or null if none could be constructed.
     */
    Date parse(String dateAsString);

    /**
     * Formats a date in the format required.
     *
     * @param date the date.  CANNOT be NULL.
     * @return the formatted date. CANNOT be NULL.
     */
    String format(Date date);
}
