// ----------------------------------------------------------------------------
// [b12] Java Source File: TracksPanel.java
//                created: 29.10.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;

/**
 * The <code>TracksPanel</code> offers a visual control to display and alter the
 * tracks for the {@linkplain PanicPlayer}.
 */
public class TracksPanel extends JPanel {

    
    Collection tracks;
    
    /**
     * Creates a new instance of <code>TracksPanel</code>.
     */
    public TracksPanel() {
        // TODO initialize with default information
    }
    
    /**
     * Sets the tracks for this <code>TracksPanel</code>.
     * @param tracks the new tracks.
     */
    public void setTracks(Collection tracks) {
        this.tracks = new ArrayList(tracks);
    }

    /* TODO create tracks panel
     *    - create TrackLabel for each track
     *    - display each tracklabel on a single row
     *    - set the pixel-seconds-factor TrackLabel.setPixelsPerSecond(int)
     *    - add MouseXXX listeners as necessary
     *    - ...
     */
}
