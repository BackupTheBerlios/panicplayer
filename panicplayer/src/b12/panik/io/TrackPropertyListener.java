// ----------------------------------------------------------------------------
// [b12] Java Source File: ComparatorTest.java
//                created: 10.01.2003
//              $Revision: 1.1 $
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
    
    public void stateHasChanged(Track t) {
        // do nothing ... implement in subclass
    }
}
