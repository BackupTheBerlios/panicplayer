// ----------------------------------------------------------------------------
// [b12] Java Source File: TrackManager.java
//                created: 15.01.2004
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.util.Collection;

import b12.panik.io.UrlTrack;
import b12.panik.util.ConstraintsException;

/**
 * Interface that allows for track adding.
 * @author kariem
 */
public interface TrackManager {
    /**
     * Adds a track to this effect, which will be managed by the effect, if the
     * track is enabled.
     * @param track the track to be added.
     * @throws ConstraintsException
     */
    void addTrack(UrlTrack track) throws ConstraintsException;
    
    /**
     * Returns a collection of the currently managed tracks.
     * @return a collection of <code>UrlTrack</code>s representing the
     *          currently managed tracks, or <code>null</code>.
     */
    Collection getTracks();
}