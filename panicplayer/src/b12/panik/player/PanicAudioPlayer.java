// ----------------------------------------------------------------------------
// [b12] Java Source File: PanicAudioPlayer.java
//                created: 28.10.2003
//              $Revision: 1.8 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Collection;

import javax.media.*;
import javax.media.protocol.DataSource;
import javax.swing.JComponent;

import b12.panik.io.MediaIOException;
import b12.panik.io.MediaInput;
import b12.panik.ui.PlayerControlPanel;
import b12.panik.util.ConstraintsException;
import b12.panik.util.LocationException;

/**
 * <p>
 * The class that allows for playing of audio files. This is the class that is
 * from used outside this package in order to play audio files. It supports
 * reading from an URL and connecting to RTP streams in order to play audio
 * data.
 * </p>
 * <p>
 * After initialization the player may be filled with the main track and the
 * additional tracks to mix in. For this purpose the methods
 * {@linkplain #setMainTrack(URL)}and {@linkplain #addTrack(URL)}are used.
 * The <code>Track</code> objects returned by these methods may be altered to
 * correctly set the start time or other properties, according to the
 * configuration or user input.
 * </p>
 */
public class PanicAudioPlayer {

   // TODO change Collection implementation if necessary (List, set, ..)
   Collection tracks;

   /** Holds the players status, to be displayed for the user */
   String status;

   private Player player;
   MediaInput input = new MediaInput();
   private PlayerControlPanel mainComponent;

   // TODO use mixeffect
   private Codec mixEffect = new MixEffect();

   /**
    * Initializes a new <code>PanicAudioPlayer</code>. This player is then
    * filled with tracks which are played at their specified positions.
    */
   public PanicAudioPlayer() {
      mainComponent = new PlayerControlPanel(null);
      // TODO implement initialization
   }

   /**
    * Returns the original input stream, in order to visualize the sound
    * befire applying the effect,
    * @return the original input stream.
    */
   public InputStream getOriginalStream() {
      // TODO implement this
      return null;
   }

   /**
    * Returns the altered stream. This method may be used to visualize the
    * sound after applying the effect.
    * @return the altered stream (output).
    */
   public InputStream getAlteredStream() {
      // TODO implement this
      return null;
   }

   /**
    * Sets the main <code>Track</code>, the "background" track for this
    * player. If the <code>Track</code> was successfully loaded it is
    * returned by this method.
    * 
    * @param location the location of the track.
    * @return the newly created track.
    * @throws LocationException if the track could not be found.
    */
   public Track setMainTrack(URL location) throws LocationException {
      // TODO load track and return it if loading was successfull
      // throw exception, if error occured
      return null;
   }

   /**
    * Adds a <code>Track</code> to this player's tracks.
    * 
    * @param location the location of the track.
    * @return the newly created track.
    * 
    * @throws LocationException if the track could not be found.
    * @throws ConstraintsException if the loaded track's properties conflict
    *          with previously entered information (e.g. the new track is
    *          longer than the background track).
    */
   public Track addTrack(URL location) throws LocationException, ConstraintsException {
      // TODO load track and return it if loading was successfull
      // add track to the collection of tracks
      // throw exception, if error occured
      return null;
   }

   /**
    * Removes a previously added <code>Track</code> from this player.
    * 
    * @param t the track to be removed
    * @return <code>true</code> if this collection changed as a result of
    *          the call
    */
   public boolean removeTrack(Track t) {
      return tracks.remove(t);
   }

   /*
    * 
    * player controls to be used from the outside
    * 
    */

   /** starts the player */
   public void start() {
      // TODO implement start
   }

   /** pauses the player */
   public void pause() {
      // TODO implement pause
   }

   /** stops the player */
   public void stop() {
      // TODO implement stop
   }

   /** plays the player at a higher speed */
   public void fastForward() {
      // TODO implement fast forward
   }

   /** rewinds the player */
   public void rewind() {
      // TODO implement rewind
   }

   /**
    * Returns the status.
    * @return the status.
    */
   public String getStatus() {
      return status;
   }

   /**
    * Returns the main component of this audio player, which is used to control
    * the player itself.
    * 
    * @param parent the parent component which will be validatet on a
    *         component change. If <code>parent</code> is <code>null</code>
    *         no update will be performed.
    * @return the main component.
    */
   public JComponent getComponent(final Container parent) {
      if (parent != null) {
         // add property change listener
         mainComponent.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
               final String name = evt.getPropertyName();
               if (name.equals(PlayerControlPanel.PLAYER_REALIZED)) {
                  // validate component ... to adjust its size
                  parent.validate();
               }
            }
         });
      }
      return mainComponent;
   }

   /**
    * Returns the visualisation component of this audio player. It should
    * update automatically and visualise the currently played sound.
    * @return the visualisation component.
    */
   public Component getVisualisationComponent() {
      return null;
   }

   /**
    * Loads the sound file for this player.
    * @param f the file to be loaded.
    * @throws MediaIOException thrown if the player cannot be instantiated
    *          or playing is not possible.
    */
   public void loadSoundFile(File f) throws MediaIOException {
      final Processor processor = input.readProcessor(f);
      System.out.println("\n\tprocessor created\n");
      mainComponent.setPlayer(processor);
   }
}