// ----------------------------------------------------------------------------
// [b12] Java Source File: TrackPropertyListener.java
//                created: 10.01.2004
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package b12.panik.io;

import javax.media.Track;
import javax.media.TrackListener;

/**
 * Listens for track changes.
 * @author kariem
 */
public interface TrackPropertyListener extends TrackListener {

    /** Property for the enabled state. */
    String PROP_ENABLED = "enabled";

    /**
     * This informs the listener for general updates.
     * @param t the track which has changed.
     * @param propertyId the property id.
     * @param oldVal the old value.
     * @param newVal the new value.
     */
    public abstract void propertyChanged(Track t, String propertyId, Object oldVal, Object newVal);
}
