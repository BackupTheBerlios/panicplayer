// ----------------------------------------------------------------------------
// [b12] Java Source File: IOUtils.java
//                created: 29.11.2003
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.IOException;
import java.net.URL;

import javax.media.Manager;
import javax.media.NoPlayerException;
import javax.media.Player;

/**
 * This class provides several methods for calls to the IO. Methods to open
 * files are supported by this class.
 * 
 * @author kariem
 */
public class IOUtils {

    public static void playSimple(URL url) throws NoPlayerException, IOException {
        getSimplePlayer(url).start();
    }
    
    public static Player getSimplePlayer(URL url) throws NoPlayerException, IOException {
        return Manager.createPlayer(url);
    }
}
