// ----------------------------------------------------------------------------
// [b12] Java Source File: IOUtils.java
//                created: 29.11.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
        Player player = Manager.createPlayer(url);
        player.start();        
    }
    
    public static void playSimple(File f) throws NoPlayerException, MalformedURLException, IOException {
        playSimple(f.toURL());
    }
}
