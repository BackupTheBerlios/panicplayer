// ----------------------------------------------------------------------------
// [b12] Java Source File: Resources.java
//                created: 01.12.2003
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Class that provides resource bundle loading.
 *
 * @author kariem
 */
public class Resources {

    private static final String BUNDLE_NAME = "b12.panik.config.messages";//$NON-NLS-1$

    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    private Resources() {
        // private constructor for utility class
    }

    /**
     * Returns the string from the resource bundle associated with <code>key</code>.
     *
     * @param key
     *            the key.
     * @return the associated string.
     */
    public static String getString(String key) {
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }
}
