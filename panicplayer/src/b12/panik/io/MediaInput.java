// ----------------------------------------------------------------------------
// [b12] Java Source File: MediaInput.java
//                created: 02.11.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.IOException;
import java.net.URL;

import javax.media.*;

import b12.panik.util.Logging;

/**
 * @author kariem
 */
public class MediaInput implements ControllerListener {

    /**
	 * Creates a new instance of <code>MediaInput</code>.
	 */
    public MediaInput() {
        // default constructor
    }

    /**
     * Reads from a given <code>URL</code> and returns an associated player.
     * 
     * @param url the url from which is read.
     * @return a realized player for the source url.
     * @throws MediaIOException if no <code>Player</code> can be found or a
     *          problem was encountered while connecting to <code>url</code>.
     */
    public Player read(URL url) throws MediaIOException {
        Logging.fine(getClass().getName(), "read(URL)", "Protocol: " + url.getProtocol());
        MediaLocator locator = new MediaLocator(url);
        if (locator == null) {
            throw new MediaIOException("Cannot build media locator for " + url);
        }
        
        // Create a player for this rtp session
        try {
            Player p = Manager.createPlayer(locator);
            p.addControllerListener(this);
            p.realize();
            return p;
        } catch (NoPlayerException e) {
            throw new MediaIOException("No player can be found.", e);
        } catch (IOException e) {
            throw new MediaIOException("Problem when trying to connect to source", e);
        }
    }

    /** @see ControllerListener#controllerUpdate(ControllerEvent) */
    public void controllerUpdate(ControllerEvent event) {
        // TODO handle controller events
        // - payload change should result in a new player to be generated
        //   => inform receiver of created player of a payload change
        // - (optional) handle new receive stream
    }

}