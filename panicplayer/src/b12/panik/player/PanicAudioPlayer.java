// ----------------------------------------------------------------------------
// [b12] Java Source File: PanicAudioPlayer.java
//                created: 28.10.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.net.URL;
import java.util.Collection;

import javax.media.Track;

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

    // TODO change this if necessary
    Collection tracks;

    /**
     * Initializes a new <code>PanicAudioPlayer</code>. This player is then
     * filled with tracks which are played at their specified positions.
     */
    public PanicAudioPlayer() {
        // TODO implement initialization
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
    public Track addTrack(URL location)
            throws LocationException, ConstraintsException {
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
}