// ----------------------------------------------------------------------------
// [b12] Java Source File: PanicAudioPlayer.java
//                created: 28.10.2003
//              $Revision: 1.19 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.awt.Component;
import java.awt.Container;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Collection;

import javax.media.*;
import javax.media.control.TrackControl;
import javax.swing.JComponent;

import b12.panik.io.*;
import b12.panik.ui.PlayerControlPanel;
import b12.panik.util.Logging;

/**
 * <p>The class that allows for playing of audio files. This is the class that
 * is from used outside this package in order to play audio files. It supports
 * reading from an URL and connecting to RTP streams in order to play audio
 * data.</p>
 * 
 * <p>After initialization the player may be filled with the main track and the
 * additional tracks to mix in. For this purpose the methods
 * {@linkplain #setMainTrack(String)}and {@linkplain #addTrack(URI)}are used.
 * The <code>Track</code> objects returned by these methods may be altered to
 * correctly set the start time or other properties, according to the
 * configuration or user input.</p>
 */
public class PanicAudioPlayer implements ControllerListener {

    // TODO change Collection implementation if necessary (List, set, ..)
    Collection tracks;

    /** Holds the players status, to be displayed for the user */
    String status;

    MediaInput input = new MediaInput();
    private final PlayerControlPanel mainComponent;

    // TODO use mixeffect
    private final MixEffect mixEffect = new MixEffect();
    
    private String mainTrack;

    /**
     * Initializes a new <code>PanicAudioPlayer</code>. This player is then
     * filled with tracks which are played at their specified positions.
     */
    public PanicAudioPlayer() {
        mainComponent = new PlayerControlPanel(null);
        // TODO implement initialization
    }

    /** Resets the player to an initial state. */
    public void reset() {
        mainComponent.reset();
        mixEffect.resetVisuals();
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
     * Adds a <code>Track</code> to this player's available tracks.
     *
     * @param uri the url of the track to be loaded.
     * @return the resulting UrlTrack.
     *
     * @throws IOException if the track could not be found.
     */
    public UrlTrack addInputTrack(URI uri) throws IOException {
        UrlTrack track = IOUtils.createTrack(uri);
        mixEffect.addInputUrlTrack(track);
        return track;
    }

    /**
     * Adds a <code>Track</code> to this player's tracks.
     *
     * @param track adds a track to the playlist.
     */
    public void addTrackForInit(UrlTrack track) {
        mixEffect.addTrackForInit(track);
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
     * Returns the effect that is used by the player.
     * @return the player's effect.
     */
    public MixEffect getEffect() {
        return mixEffect;
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
     * Loads the main sound file for this player.
     * @param urlString the url to be loaded.
     * @param update whether to update the display.
     * @throws MediaIOException thrown if the player cannot be instantiated
     *          or playing is not possible.
     */
    public void setMainTrack(String urlString, boolean update) throws MediaIOException {
        final Processor processor = input.readProcessor(urlString, this);
        Logging.fine("Sound file " + urlString + " opened, loading processor");
        this.mainTrack = urlString;
        if (update) {
            mixEffect.updateLength(urlString);
        }
        mainComponent.setPlayer(processor);
    }
    
    /**
     * Loads the sound file for this player.
     * @param urlString the url to be loaded.
     * @throws MediaIOException thrown if the player cannot be instantiated
     *          or playing is not possible.
     */
    public void setMainTrack(String urlString) throws MediaIOException {
        setMainTrack(urlString, true);
    }
    
    /** May be used to update the players display. */
    public void update() {
        if (mainTrack != null) {
            // try updating mix effect
            mixEffect.updateLength(mainTrack);
        }
    }
    
    /** @see ControllerListener#controllerUpdate(ControllerEvent) */
    public void controllerUpdate(ControllerEvent event) {
        // TODO handle controller events
        // - payload change should result in a new player to be generated
        //   => inform receiver of created player of a payload change
        // - (optional) handle new receive stream
        // in configured state add mixeffect codec for processing

        if (event instanceof ConfigureCompleteEvent) {

            Processor p = (Processor) event.getSource();
            // So I can use it as a player.
            p.setContentDescriptor(null);

            TrackControl[] trackControls = p.getTrackControls();

            final Codec[] codecChain = new Codec[]{mixEffect};
            for (int i = 0; i < trackControls.length; i++) {
                try {
                    Logging.info("Original input format: " + trackControls[i].getFormat());
                    trackControls[i].setCodecChain(codecChain);
                } catch (UnsupportedPlugInException e) {
                    Logging.warning("Unsupported plugin", e);
                }
            }
        }
    }
}
