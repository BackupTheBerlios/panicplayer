// ----------------------------------------------------------------------------
// [b12] Java Source File: DynamicTrackLabel.java
//                created: 12.01.2004
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Dimension;
import java.awt.event.*;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import b12.panik.io.UrlTrack;

/**
 * A track label that is capable of automatic resizing.
 * @author kariem
 */
public class DynamicTrackLabel extends TrackLabel {

    /**
     * Creates a new instance of <code>DynamicTrackLabel</code> from the
     * information found in the given <code>TrackLabel</code>.
     * @param label the other label
     */
    public DynamicTrackLabel(TrackLabel label) {
        this(label.track);
        addMouseListener(new TrackMouseAdapter());
    }

    /**
     * Creates a new instance of <code>DynamicTrackLabel</code>.
     * @param track the track which is rendered by this label.
     */
    public DynamicTrackLabel(UrlTrack track) {
        super(track, false);
    }

    /** @see java.awt.Component#setLocation(int, int) */
    public void setLocation(int x, int y) {
        // avoid rounding errors
        if (x < 0) {
            x = 0;
        }
        if (x == getX()) {
            // no change on x-axis => do nothing
            return;
        }
        super.setLocation(x, y);

        if (x == 0) {
            track.setStartTime(0);
        } else {
            track.setStartTime(x / (float) pixelsPerSecond);

        }
        updateToolTip();
    }

    /** @see javax.swing.JComponent#getPreferredSize() */
    public Dimension getPreferredSize() {
        Dimension preferred = super.getPreferredSize();
        preferred.width = (int) (track.getDuration().getSeconds() * pixelsPerSecond);
        if (preferred.width < 10) {
            preferred.width = 10;
        }
        setPreferredSize(preferred);
        return preferred;
    }

    JPopupMenu createPopup() {
        JPopupMenu menu = new JPopupMenu("Properties");
        final JCheckBoxMenuItem cbEnabled = new JCheckBoxMenuItem("Enabled");
        final boolean trackEnabled = track.isEnabled();
        cbEnabled.setSelected(trackEnabled);
        cbEnabled.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setEnabled(!trackEnabled);
            }
        });
        menu.add(cbEnabled);

        return menu;
    }

    class TrackMouseAdapter extends MouseAdapter {
        /** @see MouseAdapter#mouseReleased(MouseEvent) */
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger()) {
                createPopup().show(DynamicTrackLabel.this, e.getX(), e.getY());
            }
        }
    }
}