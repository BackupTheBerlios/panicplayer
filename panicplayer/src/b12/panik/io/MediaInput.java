// ----------------------------------------------------------------------------
// [b12] Java Source File: MediaInput.java
//                created: 02.11.2003
//              $Revision: 1.11 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.media.*;
import javax.media.protocol.DataSource;

import b12.panik.player.MixEffect;
import b12.panik.util.Logging;

/**
 * Input class for generic media.
 * @author kariem
 */
public class MediaInput {

    // register effect plugin
    static {
        // Name of the new plugin
        String s = new String(MixEffect.class.getName());
        // Add the new plug-in to the plug-in registry
        PlugInManager.addPlugIn(s, MixEffect.FORMATS_INPUT, MixEffect.FORMATS_OUTPUT,
                PlugInManager.CODEC);
        try {
            // Save the changes to the plug-in registry
            PlugInManager.commit();
        } catch (IOException e) {
            Logging.warning("Effect plugin could not be registered with plugin manager.", e);
        }
    }

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
     * @param listener the controller listener.
     * @return a realized player for the source url.
     * @throws MediaIOException if no <code>Player</code> can be found or a
     *          problem was encountered while connecting to <code>url</code>.
     */
    public Player read(URL url, ControllerListener listener) throws MediaIOException {
        Logging.fine(getClass().getName(), "read(URL)", "Protocol: " + url.getProtocol());
        MediaLocator locator = new MediaLocator(url);
        if (locator == null) {
            throw new MediaIOException("Cannot build media locator for " + url);
        }
        // Create a player for this rtp session
        try {
            Player p = Manager.createPlayer(locator);
            p.addControllerListener(listener);
            return p;
        } catch (NoPlayerException e) {
            throw new MediaIOException("No player can be found.", e);
        } catch (IOException e) {
            throw new MediaIOException("Problem when trying to connect to source", e);
        }
    }

    /**
     * Reads from a data source.
     * @param ds the data source.
     * @return a corresponding player.
     * @throws MediaIOException if an error occured when trying to connect to
     *          the source, or no player could be created.
     */
    public Player read(DataSource ds) throws MediaIOException {
        try {
            Player p = Manager.createPlayer(ds);
            return p;
        } catch (NoPlayerException e) {
            throw new MediaIOException("No player can be found.", e);
        } catch (IOException e) {
            throw new MediaIOException("Problem when trying to connect to source", e);
        }
    }

    /**
     * Creates a processor from an URL.
     * @param url the url.
     * @param listener the controller listener.
     * @return a processor.
     * @throws MediaIOException if an error occured when trying to connect to
     *          the source, or no processor could be created.
     */
    public Processor readProcessor(String url, ControllerListener listener)
            throws MediaIOException {
        // log loaded processor
        int indexColon = url.indexOf(':');
        String message;
        if (indexColon != -1) {
            message = "Protocol: " + url.substring(0, indexColon);
        } else {
            message = "Locator: " + url;
        }
        Logging.fine(getClass().getName(), "read(URL)", message);

        // load processor
        MediaLocator locator = new MediaLocator(url);
        if (locator == null) {
            throw new MediaIOException("Cannot build media locator for " + url);
        }
        // Create a player for this rtp session
        try {
            Processor p = Manager.createProcessor(locator);
            if (listener != null) {
                p.addControllerListener(listener);
            }
            p.configure();
            return p;
        } catch (NoProcessorException e) {
            throw new MediaIOException("No processor can be found.", e);
        } catch (IOException e) {
            throw new MediaIOException("Problem when trying to connect to source", e);
        }
    }

    /**
     * Creates a processor from a file.
     * @param f the file.
     * @param listener the controller listener.
     * @return a processor.
     * @throws MediaIOException if an error occured when trying to connect to
     *          the source, or no processor could be created.
     */
    public Processor readProcessor(File f, ControllerListener listener) throws MediaIOException {
        return readProcessor(f.toString(), listener);
    }
}
