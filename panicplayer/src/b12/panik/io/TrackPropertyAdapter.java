// ----------------------------------------------------------------------------
// [b12] Java Source File: TrackPropertyAdapter.java
//                created: 16.01.2004
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import javax.media.Track;
import javax.media.TrackListener;

/**
 * Listens for track changes.
 * @author kariem
 */
public class TrackPropertyAdapter implements TrackPropertyListener {

    /** @see TrackListener#readHasBlocked(Track) */
    public void readHasBlocked(Track t) {
        // do nothing ... implement in subclass (if necessary)
    }

    /** @see TrackPropertyListener#propertyChanged(Track, String, Object, Object) */
    public void propertyChanged(Track t, String propertyId, Object oldVal, Object newVal) {
        // do nothing ... implement in subclass (if necessary)
    }
}
