// ----------------------------------------------------------------------------
// [b12] Java Source File: TrackLabel.java
//                created: 29.10.2003
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Color;
import java.awt.Dimension;

import javax.media.Track;
import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * This class is used to represent a <code>Track</code> visually as a label.
 * It provides several access points to the <code>Track</code> s data.
 */
public class TrackLabel extends JLabel {

    private static final Color COLOR_BORDER = Color.GRAY;
    private static final Color COLOR_CONTENT = Color.DARK_GRAY;
    private static final Color COLOR_CONTENT_DISABLED = Color.LIGHT_GRAY;
    private static final Color COLOR_FOREGROUND = Color.CYAN;
    private static final Color COLOR_FOREGROUND_DISABLED = Color.GRAY;

    private Track track;

    private static int pixelsPerSecond = 10;

    /**
	 * Creates a new instance of <code>TrackLabel</code>. The <code>Track</code>
	 * has to be set manually.
	 */
    public TrackLabel() {
        this(null);
    }

    /**
	 * Creates a new instance of <code>TrackLabel</code> with an associated
	 * <code>Track</code> object.
	 *
	 * @param track the track which is rendered in this object.
	 */
    public TrackLabel(Track track) {
        // set basic visual representation
        setBorder(BorderFactory.createLineBorder(COLOR_BORDER));

        if (track != null) {
            setTrack(track);
        }
    }

    /**
	 * Sets the amount of pixels that is used per second of the rendered track
	 * in order to draw the <code>Track</code> in correct proportions.
	 *
	 * @param factor the amount of pixels per second.
	 */
    public static void setPixelsPerSecond(int factor) {
        pixelsPerSecond = factor;
    }

    /**
	 * Returns the track.
	 *
	 * @return the track.
	 */
    public Track getTrack() {
        return track;
    }
    /**
	 * Sets the track.
	 *
	 * @param track The track.
	 */
    public void setTrack(Track track) {
        this.track = track;
        // get properties from track and manipulate label accordingly

        // set enabled/disabled
        setEnabled(track.isEnabled());
        // set preferred size
        Dimension preferred = getPreferredSize();
        preferred.width = (int) (track.getDuration().getSeconds() * pixelsPerSecond);
        setPreferredSize(preferred);

        // set tooltip to display information on the track
        StringBuffer tooltip = new StringBuffer();
        tooltip.append("Start: " + track.getStartTime().getSeconds() + "\n");
        tooltip.append("Duration: " + track.getDuration().getSeconds() + "\n");
        tooltip.append("Format: " + track.getFormat().getEncoding() + "\n");
        setToolTipText(tooltip.toString());
    }

    /** @see java.awt.Component#setEnabled(boolean) */
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (enabled) {
            setBackground(COLOR_CONTENT);
            setForeground(COLOR_FOREGROUND);
        } else {
            setBackground(COLOR_CONTENT_DISABLED);
            setForeground(COLOR_FOREGROUND_DISABLED);
        }

    }

}