// ----------------------------------------------------------------------------
// [b12] Java Source File: Resources.java
//                created: 01.12.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.config;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * 
 * @author kariem
 */
public class Resources {

    private static final String BUNDLE_NAME = "b12.panik.config.messages";//$NON-NLS-1$
    private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle.getBundle(BUNDLE_NAME);

    /**
     * Creates a new instance of <code>Resources</code>.
     * 
     */
    private Resources() {

        // TODO Auto-generated constructor stub
    }
    /**
     * @param key
     * @return
     */
    public static String getString(String key) {
        // TODO Auto-generated method stub
        try {
            return RESOURCE_BUNDLE.getString(key);
        } catch (MissingResourceException e) {
            return key;
        }
    }
}