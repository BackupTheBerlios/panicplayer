// ----------------------------------------------------------------------------
// [b12] Java Source File: MediaInput.java
//                created: 02.11.2003
//              $Revision: 1.7 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.*;
import javax.media.control.TrackControl;
import javax.media.format.AudioFormat;

import b12.panik.player.MixEffect;
import b12.panik.util.Logging;

/**
 * Input class for generic media.
 * @author kariem
 */
public class MediaInput implements ControllerListener {
    // register effect plugin
    /*
     static {
     // Name of the new plugin
     String MixEffectPlugin = GainEffect.class.getName();
     // Add the new plug-in to the plug-in registry
     PlugInManager.addPlugIn(MixEffectPlugin, MixEffect.FORMATS_INPUT, MixEffect.FORMATS_OUTPUT, PlugInManager.CODEC);
     try {
     // Save the changes to the plug-in registry
     PlugInManager.commit();
     } catch (IOException e) {
     Logging.warning("Effect plugin could not be registered with plugin manager.", e);
     }
     }*/
    static {
        // Name of the new plugin
        String GainPlugin = new String("COM.mybiz.media.GainEffect");
        // Supported input Formats
        Format[] supportedInputFormats = new Format[]{new AudioFormat(AudioFormat.LINEAR,
                Format.NOT_SPECIFIED, 16, Format.NOT_SPECIFIED, AudioFormat.LITTLE_ENDIAN,
                AudioFormat.SIGNED, 16, Format.NOT_SPECIFIED, Format.byteArray)};
        // Supported output Formats
        Format[] supportedOutputFormats = new Format[]{new AudioFormat(AudioFormat.LINEAR,
                Format.NOT_SPECIFIED, 16, Format.NOT_SPECIFIED, AudioFormat.LITTLE_ENDIAN,
                AudioFormat.SIGNED, 16, Format.NOT_SPECIFIED, Format.byteArray)};
        // Add the new plug-in to the plug-in registry
        PlugInManager.addPlugIn(GainPlugin, supportedInputFormats, supportedOutputFormats,
                PlugInManager.EFFECT);
        try {
            // Save the changes to the plug-in registry
            PlugInManager.commit();
        } catch (IOException e) {
            e.printStackTrace();
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
            return p;
        } catch (NoPlayerException e) {
            throw new MediaIOException("No player can be found.", e);
        } catch (IOException e) {
            throw new MediaIOException("Problem when trying to connect to source", e);
        }
    }
    //teo
    public Processor readProcessor(URL url) throws MediaIOException {
        Logging.fine(getClass().getName(), "read(URL)", "Protocol: " + url.getProtocol());
        MediaLocator locator = new MediaLocator(url);
        if (locator == null) {
            throw new MediaIOException("Cannot build media locator for " + url);
        }
        // Create a player for this rtp session
        try {
            Processor p = Manager.createProcessor(locator);
            p.addControllerListener(this);
            return p;
        } catch (NoProcessorException e) {
            throw new MediaIOException("No processor can be found.", e);
        } catch (IOException e) {
            throw new MediaIOException("Problem when trying to connect to source", e);
        }
    }
    //fin teo
    //teo
    public Processor readProcessor(File f) throws MediaIOException {
        try {
            return readProcessor(f.toURL());
        } catch (MalformedURLException e) {
            throw new MediaIOException("Path to file could not be parsed as URL.", e);
        }
    }
    //fin teo
    /**
     * Reads a file.
     * @param f the file.
     * @return the player for reading the file.
     * @throws MediaIOException if the file could not be parsed.
     */
    public Player read(File f) throws MediaIOException {
        try {
            return read(f.toURL());
        } catch (MalformedURLException e) {
            throw new MediaIOException("Path to file could not be parsed as URL.", e);
        }
    }
    /** @see ControllerListener#controllerUpdate(ControllerEvent) */
    public void controllerUpdate(ControllerEvent event) {
        // TODO handle controller events
        // - payload change should result in a new player to be generated
        //   => inform receiver of created player of a payload change
        // - (optional) handle new receive stream
        // in configured state add mixeffect codec for processing
        //teo
        System.out.println("controller update appele, l event est: " + event.toString());
        //fin teo
        if (event instanceof ConfigureCompleteEvent) {
            //teo
            System.out.println("le si est realise");
            //fin teo
            Processor p = (Processor) event.getSource();
            TrackControl[] trackControls = p.getTrackControls();
            for (int i = 0; i < trackControls.length; i++) {
                // TODO set codec mixeffect
                //teo: 
                try {
                    Codec codecEssai = new MixEffect();
                    Format essai= (Format) trackControls[i].getFormat();
                    //codecEssai.setInputFormat(essai);
                    //codecEssai.setOutputFormat(essai);
                    System.err.println("format son:"+essai);
                    //Codec[] codecEssai=new Codec[] {new MixEffect()});
                    //trackControls[i].setCodecChain(new Codec[] {new MixEffect()});
                    trackControls[i].setCodecChain(new Codec[]{codecEssai});
                    System.err.println("le setCodecChain est realise");
                    String MixEffectPlugin = MixEffect.class.getName();
                    // Add the new plug-in to the plug-in registry
                    try {
                        // Save the changes to the plug-in registry
                        PlugInManager.commit();
                    } catch (IOException e) {
                        Logging
                                .warning(
                                        "Effect plugin could not be registered with plugin manager.",
                                        e);
                    }
                } catch (Exception e) {
                }
                //fin teo
                // trackControls[i].setCodecChain(codecs);
            }
        }
        //teo
        System.err.println("le controller update est fini");
        //fin teo
    }
}