// ----------------------------------------------------------------------------
// [b12] Java Source File: TracksPanel.java
//                created: 29.10.2003
//              $Revision: 1.5 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.dnd.DnDConstants;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JPanel;

import b12.panik.ui.util.RoundBorder;

/**
 * The <code>TracksPanel</code> offers a visual control to display and alter
 * the tracks for the {@linkplain PanicPlayer}.
 */
public class TracksPanel extends JPanel {

    Collection tracks;

    /**
     * Creates a new instance of <code>TracksPanel</code>.
     */
    public TracksPanel() {
        setLayout(new GridBagLayout());
        setBorder(new RoundBorder("Drop Me", new Insets(1, 1, 1, 1)));
        setBackground(Color.LIGHT_GRAY);

        // drop panel
        JPanel dropPanel = new JPanel();
        final Dimension dimDrop = new Dimension(300, 200);
        dropPanel.setPreferredSize(dimDrop);
        dropPanel.setOpaque(false); //in order to see border of panel
        
        // drag panel
        JPanel availablePanel = new JPanel();
        Dimension dimAvailable = new Dimension(300, 100);
        availablePanel.setPreferredSize(dimAvailable);
        availablePanel.setBorder(new RoundBorder("Drag Me"));
        availablePanel.setOpaque(false); //in order to see border of panel

        add(dropPanel, new GridBagConstraints(0, 0, 1, 1, 1, .6, GridBagConstraints.CENTER,
                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
        add(availablePanel, new GridBagConstraints(0, 1, 1, 1, 1, .4,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0,
                0));
        
		DragObject dragLabel = new DragObject("DragME",DnDConstants.ACTION_COPY_OR_MOVE);
			dragLabel.setBackground(Color.white);
			dragLabel.setOpaque(true);    
			availablePanel.add(dragLabel);
			
		DropObject dropLabel = new DropObject("DropME",DnDConstants.ACTION_COPY_OR_MOVE);
			dropLabel.setBackground(Color.yellow);
			dropLabel.setOpaque(true);        
			dropPanel.add(dropLabel);
	
        // TODO initialize with default information
    }
    
    
    /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
    protected void paintComponent(Graphics g) {
        if (ui != null) {
            Graphics scratch = g.create();
            try {
                scratch.setColor(getBackground());
                ((RoundBorder) getBorder()).paintInterior(scratch, 0, 0, getWidth(), getHeight());
            }
            finally {
                scratch.dispose();
            }
        }
    }
    

    /**
     * Sets the tracks for this <code>TracksPanel</code>.
     * 
     * @param tracks
     *            the new tracks.
     */
    public void setTracks(Collection tracks) {
        this.tracks = new ArrayList(tracks);
    }

    /*
     * TODO create tracks panel - create TrackLabel for each track - display
     * each tracklabel on a single row - set the pixel-seconds-factor
     * TrackLabel.setPixelsPerSecond(int) - add MouseXXX listeners as necessary -
     * ...
     */
}
