// ----------------------------------------------------------------------------
// [b12] Java Source File: TrackManager.java
//                created: 15.01.2004
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.player;

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
}