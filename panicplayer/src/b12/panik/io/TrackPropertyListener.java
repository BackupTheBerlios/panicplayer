// ----------------------------------------------------------------------------
// [b12] Java Source File: ComparatorTest.java
//                created: 10.01.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import javax.media.Track;
import javax.media.TrackListener;

/**
 * Listens for track changes.
 * @author kariem
 */
public abstract class TrackPropertyListener implements TrackListener {

    /** @see TrackListener#readHasBlocked(Track) */
    public void readHasBlocked(Track t) {
        // do nothing ... implement in subclass
    }
    
    /**
     * This informs the listener for general updates.
     * @param t the track which has changed.
     */
    public abstract void stateHasChanged(Track t);
}
