package org.jasig.cas.util;

import org.jasig.cas.server.util.DateParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Simple, performant, implementation that keeps one simple date format instance per thread per date format type.
 * <p>
 * Classes may want to extend this to provide a default formatting.
 * <p>
 * The SimpleDateFormat's used in this are automatically set to the UTC time zone.  This puts everything in the format
 * that Java started with.
 *
 * @author Scott Battaglia
 * @version $Revision$ $Date$
 * @since 3.5
 */
public class ThreadLocalDateFormatDateParser implements DateParser {

    private final ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal;

    public ThreadLocalDateFormatDateParser(final String dateFormat) {
        this.simpleDateFormatThreadLocal = new ThreadLocal<SimpleDateFormat>() {

        @Override
        protected SimpleDateFormat initialValue() {
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            return simpleDateFormat;
        }
    };

    }

    public final Date parse(final String dateAsString) {
        try {
            return this.simpleDateFormatThreadLocal.get().parse(dateAsString);
        } catch (final ParseException e) {
            return null;
        }
    }

    public final String format(final Date date) {
        return this.simpleDateFormatThreadLocal.get().format(date);
    }
}
