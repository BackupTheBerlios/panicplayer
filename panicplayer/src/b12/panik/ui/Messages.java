// ----------------------------------------------------------------------------
// [MM1 - Massenpanik]
//       Java Source File: Messages.java
//                  $Date: 2003/12/28 18:21:41 $
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Resource bundle to access ui messages.
 * @author kariem
 */
public class Messages {

   private static final String BUNDLE_NAME = "b12.panik.ui.ui";//$NON-NLS-1$

   private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
         .getBundle(BUNDLE_NAME);

   private Messages() {
      // private constructor for utility class
   }

   /**
    * Returns the corresponding value for the given key in the resource bundle.
    * @param key the key.
    * @return the associated string.
    */
   public static String getString(String key) {
      try {
         return RESOURCE_BUNDLE.getString(key);
      } catch (MissingResourceException e) {
         return '!' + key + '!';
      }
   }
}