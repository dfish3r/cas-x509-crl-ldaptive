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
