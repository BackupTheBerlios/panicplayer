// ----------------------------------------------------------------------------
// [b12] Java Source File: MediaInput.java
//                created: 02.11.2003
//              $Revision: 1.8 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.media.*;
import javax.media.control.TrackControl;
import javax.media.protocol.DataSource;

import b12.panik.player.MixEffect;
import b12.panik.util.Logging;

/**
 * Input class for generic media.
 * @author kariem
 */
public class MediaInput implements ControllerListener {

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
         p.configure();
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

      System.out.println(event.toString());

      if (event instanceof ConfigureCompleteEvent) {
         
         Processor p = (Processor) event.getSource();
         // So I can use it as a player.
         p.setContentDescriptor(null);
         
         TrackControl[] trackControls = p.getTrackControls();

         /*
          Format format = MixEffect.FORMATS_INPUT[0];
          if (!setTrackFormat(trackControls, format)) {
          Logging.warning("Could not transcode any track to " + format);
          }
          */
         final Codec[] codecChain = new Codec[]{new MixEffect()};
         System.out.println("number of track controls: " + trackControls.length);
         for (int i = 0; i < trackControls.length; i++) {
            try {
               System.out.println(trackControls[i].getFormat());
               trackControls[i].setCodecChain(codecChain);
            } catch (UnsupportedPlugInException e) {
               Logging.warning("Not supported plugin", e);
            }
         }
      }
   }

   /*
   boolean setTrackFormat(TrackControl[] trackControls, Format format) {

      Format[] supported;
      Format f;

      for (int i = 0; i < trackControls.length; i++) {
         // TODO set codec mixeffect
         //teo: 
         try {
            Format essai = trackControls[i].getFormat();
            System.out.println("format son:" + essai);
            System.out.println("\t datatype:" + essai.getDataType().getName());
            System.out.println("\t encoding:" + essai.getEncoding());
            //trackControls[i].setCodecChain(new Codec[]{effect});
            supported = trackControls[i].getSupportedFormats();
            if (supported == null) {
               continue;
            }

            for (int j = 0; j < supported.length; j++) {

               if (format.matches(supported[j])
                     && (f = format.intersects(supported[j])) != null
                     && trackControls[i].setFormat(f) != null) {

                  // Success.
                  return true;
               }
            }
         } catch (Exception e) {
            Logging.warning("track controls error", e);
         }
      }
      return false;
   }
   */
}