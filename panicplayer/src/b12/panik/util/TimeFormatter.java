// ----------------------------------------------------------------------------
// [b12] Java Source File: TimeFormatter.java
//                created: 30.11.2003
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package b12.panik.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Simple time formatter.
 * @author kariem
 */
public class TimeFormatter {

    static SimpleDateFormat format = new SimpleDateFormat("mm:ss.SSS");

    /**
     * Formats the time given as long value.
     * @param time the time.
     * @return a string representing the time value.
     */
    public static String format(long time) {
        return format.format(new Date(time));
    }

    /**
     * Parses <code>s</code> and returns the corresponding time in milliseconds.
     *
     * @param s the string to parse.
     * @return the time in milliseconds.
     * @throws ParseException if <code>s</code> cannot be parsed.
     */
    public static long parse(String s) throws ParseException {
        long time = format.parse(s).getTime();
        if (time < 0) {
            // add one hour
            time += 3600000;
        }
        return time;
    }

    /**
     * Parses s and ignores the thrown Exception.
     * @param s the string to parse
     * @return the time in milliseconds or <i>0</i> if an error occured while
     *          parsing.
     */
    public static long parseFast(String s) {
        try {
            return parse(s);
        } catch (ParseException e) {
            return 0;
        }
    }
}