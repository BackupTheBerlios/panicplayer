// ----------------------------------------------------------------------------
// [b12] Java Source File: IOUtils.java
//                created: 29.11.2003
//              $Revision: 1.4 $
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

    /**
     * Plays the content of a url.
     * 
     * @param url
     *            the url.
     * @throws NoPlayerException
     *             if no player could be created for the resource.
     * @throws IOException
     *             if an IO problem occured while trying to load the resource.
     */
    public static void playSimple(URL url) throws NoPlayerException, IOException {
        getSimplePlayer(url).start();
    }

    /**
     * Returns a simple player for the given resource.
     * 
     * @param url
     *            the url.
     * @return a player capable of playing the data found in <code>url</code>.
     * @throws NoPlayerException
     *             if no player could be created.
     * @throws IOException
     *             if an IO problem occured while trying to load the resource.
     */
    public static Player getSimplePlayer(URL url) throws NoPlayerException, IOException {
        return Manager.createPlayer(url);
    }
}
