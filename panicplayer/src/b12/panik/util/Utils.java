// ----------------------------------------------------------------------------
// [b12] Java Source File: Utils.java
//                created: 29.10.2003
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package b12.panik.util;

/**
 * Holds some utility methods.
 */
public class Utils {

    private Utils() {
        // private constructor for utility class
    }

    /**
     * Converts a <code>long</code> value to an <code>int</code> value.
     *
     * @param longInput the input as <code>int</code>.
     * @return the input as <code>int</code>, while too small or too big
     *          input values are cut down to maximum or minimum
     *          <code>int</code> values.
     */
    public static int convertToInt(long longInput) {
        if (longInput > Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        if (longInput < Integer.MIN_VALUE + 1) {
            // for Math.abs (2 complement)
            return Integer.MIN_VALUE + 1;
        }
        return (int) longInput;
    }

    /**
     * Returns an error string that shows the stack trace of the given
     * throwable.
     * 
     * @param t the throwable.
     * @param message the message.
     * @return a String containing the message plus the stack trace of the
     *          throwable. If <code>t</code> is <code>null</code> then only the
     *          given message is returned.
     */
    public static String getErrorString(Throwable t, String message) {
        if (t != null) {
            StringBuffer buf = new StringBuffer(message);
            buf.append('\n');
            StackTraceElement[] traces = t.getStackTrace();
            for (int i = 0; i < traces.length && i < 3; i++) {
                for (int j = 0; j < i; j++) {
                    buf.append("  ");
                }
                buf.append(traces[i]);
                buf.append('\n');
            }
            message = buf.toString();
        }
        return message;
    }
}