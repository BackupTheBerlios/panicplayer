// ----------------------------------------------------------------------------
// [b12] Java Source File: TrackPropertyPanel.java
//                created: 31.10.2003
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import javax.media.Track;
import javax.swing.JPanel;

/**
 * This panel is used to show the information contained in a <code>Track</code>.
 * The information may be altered using common UI controls (buttons, check
 * boxes, sliders, ...).
 */
public class TrackPropertyPanel extends JPanel {

    Track track;

    /**
     * Creates a new instance of <code>TrackPropertyPanel</code>.
     */
    public TrackPropertyPanel() {
        // TODO design user interface
    }

    /**
     * Returns the track.
     * @return the track.
     */
    public Track getTrack() {
        return track;
    }
    /**
     * Sets the track.
     * @param track The track.
     */
    public void setTrack(Track track) {
        this.track = track;
    }
}
