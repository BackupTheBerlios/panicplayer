// ----------------------------------------------------------------------------
// [b12] Java Source File: LocationException.java
//                created: 28.10.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.util;

/**
 * This Exception is used to indicate errors that are related to an incorrect
 * location. It can be compared to a more sophisticated IOException.
 */
public class LocationException extends Exception {

    /**
     * Creates a new <code>locationException</code> with the message
     * <code>message</code>.
     * @param message the message.
     */
    public LocationException(String message) {
        super(message);
    }

    /**
     * Creates a new <code>LocationException</code> with the cause
     * <code>cause</code>.
     * @param cause a <code>Throwable</code> that shows the cause.
     */
    public LocationException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new <code>LocationException</code> with a cause and an
     * additional message.
     *
     * @param message the message.
     * @param cause the cause.
     */
    public LocationException(String message, Throwable cause) {
        super(message, cause);
    }

}