// ----------------------------------------------------------------------------
// [b12] Java Source File: ConfigurationException.java
//                created: 26.10.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.config;

/**
 * This Exceptions is used to indicate errors in the configuration. 
 */
public class ConfigurationException extends Exception {

    /**
     * Creates a new <code>ConfigurationException</code> with the message
     * <code>message</code>.
     * @param message the message.
     */
    public ConfigurationException(String message) {
        super(message);
    }

    /**
     * Creates a new <code>ConfigurationException</code> with the cause
     * <code>cause</code>.
     * @param cause a <code>Throwable</code> that shows the cause.
     */
    public ConfigurationException(Throwable cause) {
        super(cause);
    }

    /**
     * Creates a new <code>ConfigurationException</code> with a cause and an
     * additional message.
     * 
     * @param message the message.
     * @param cause the cause.
     */
    public ConfigurationException(String message, Throwable cause) {
        super(message, cause);
    }

}
