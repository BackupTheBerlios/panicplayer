// ----------------------------------------------------------------------------
// [b12] Java Source File: LocationException.java
//                created: 28.10.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.util;

/**
 * This Exception is used to indicate errors that are related to illegal
 * combination of values that would result in corrupt configurations. For
 * example: the background track of a player should be longer than the samples
 * that are mixed in.
 */
public class ConstraintsException extends Exception {

    /**
     * Creates a new <code>Exception</code> with the message
     * <code>message</code>.
     * @param message the message.
     */
    public ConstraintsException(String message) {
        super(message);
    }

    /**
     * Creates a new <code>Exception</code> with the cause
     * <code>cause</code>.
     * @param cause a <code>Throwable</code> that shows the cause.
     */
    public ConstraintsException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new <code>Exception</code> with a cause and an
     * additional message.
     *
     * @param message the message.
     * @param cause the cause.
     */
    public ConstraintsException(String message, Throwable cause) {
        super(message, cause);
    }
}