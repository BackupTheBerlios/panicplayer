// ----------------------------------------------------------------------------
// [b12] Java Source File: MediaIOException.java
//                created: 01.11.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.io;


/**
 * This Exception represents errors than occur while accessing IO resources.
 * There are several different causes for Exceptions within this package, which
 * are encapsulated in this class.
 */
public class MediaIOException extends Exception {

    /**
     * Creates a new <code>Exception</code> with the message
     * <code>message</code>.
     * @param message the message.
     */
    public MediaIOException(String message) {
        super(message);
    }

    /**
     * Creates a new <code>Exception</code> with the cause
     * <code>cause</code>.
     * @param cause a <code>Throwable</code> that shows the cause.
     */
    public MediaIOException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new <code>Exception</code> with a cause and an
     * additional message.
     *
     * @param message the message.
     * @param cause the cause.
     */
    public MediaIOException(String message, Throwable cause) {
        super(message, cause);
    }
}
