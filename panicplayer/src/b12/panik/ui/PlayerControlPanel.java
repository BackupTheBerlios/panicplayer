// ----------------------------------------------------------------------------
// [b12] Java Source File: PlayerControlPanel.java
//                created: 29.11.2003
//              $Revision: 1.10 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.media.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import b12.panik.util.Logging;

/**
 * A panel that provides controls for a simple audio player.
 * @author kariem
 */
public class PlayerControlPanel extends JPanel {

    /** This property indicates that the player is realized. */
    public static final String PLAYER_REALIZED = "player_realized";

    private JLabel infoLabel;
    JLabel visLabel;

    Player player;

    Component centerComponent;
    Component lowerComponent;

    /**
     * Creates a new instance of <code>PlayerControlPanel</code>.
     * @param p the player.
     */
    public PlayerControlPanel(Player p) {
        super(new BorderLayout());
        setOpaque(false);
        this.player = p;
        infoLabel = new JLabel();
        infoLabel.setHorizontalAlignment(SwingConstants.CENTER);
        visLabel = new JLabel();
        visLabel.setHorizontalAlignment(SwingConstants.CENTER);
        setPlayer(player);
    }

    /**
     * Returns the player.
     * @return the player.
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Sets the player.
     * @param p The player.
     */
    public void setPlayer(Player p) {
        if (this.player != null) {
            // stop the old player
            this.player.stop();
            this.player.deallocate();
        }
        this.player = p;

        if (player == null) {
            removePlayerComponents();
            return; // nothing more to do
        }

        // TODO use PanicAudioPlayer in here
        // construct listener with this component
        ControllerListener listener = new ControllerAdapter() {

           public void prefetchComplete(PrefetchCompleteEvent e) {
               Logging.fine("Player prefetched. Updating components");
                // get visual component and control component from player
                final Component vc = player.getVisualComponent();
                removeCenterComponent();
                if (vc != null) {
                    centerComponent = vc;
                } else {
                    // no vis component, replace with label
                    visLabel.setText("No visual component for player.");
                    //PSlider volSlider = new PSlider(-50, 50, 0, "Vol");
                    //visLabel.add(volSlider);
                    centerComponent = visLabel;
                }
                add(centerComponent, BorderLayout.CENTER);
                final Component cpc = player.getControlPanelComponent();
                removeLowerComponent();
                if (cpc != null) {
                    add(cpc, BorderLayout.SOUTH);
                    lowerComponent = cpc;
                }
                throwProperty(PLAYER_REALIZED);
            }
        };
        player.addControllerListener(listener);
        player.prefetch();
    }

    void removeCenterComponent() {
        if (centerComponent != null) {
            remove(centerComponent);
        }
    }

    void removeLowerComponent() {
        if (lowerComponent != null) {
            remove(lowerComponent);
        }
    }

    void throwProperty(String prop) {
        firePropertyChange(prop, null, null);
    }

    /** Removes the components from the panel that were created by the player */
    private void removePlayerComponents() {
        removeCenterComponent();
        removeLowerComponent();
        infoLabel.setText("No main track loaded.");
        centerComponent = infoLabel;
        add(centerComponent, BorderLayout.CENTER);
    }
}