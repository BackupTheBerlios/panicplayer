// ----------------------------------------------------------------------------
// [b12] Java Source File: PlayerControlPanel.java
//                created: 29.11.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.media.*;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

/**
 * A panel that provides controls for a simple audio player. 
 * @author kariem
 */
public class PlayerControlPanel extends JPanel {

    static final String PLAYER_REALIZED = "player_realized";
    
    private JLabel infoLabel;
    JLabel visLabel;
    
    Player player;
    
    Component centerComponent;
    Component lowerComponent;

    /**
     * Creates a new instance of <code>PlayerControlPanel</code>.
     */
    public PlayerControlPanel(Player p) {
        super(new BorderLayout());
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
     * @param player The player.
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
            public void realizeComplete(RealizeCompleteEvent event) {
                // get visual component and control component from player
                final Component vc = player.getVisualComponent();
                removeCenterComponent();
                if (vc != null) {
                    centerComponent = vc;
                } else {
                    // no vis component, replace with label
                    visLabel.setText("No visual component for player.");
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
        player.realize();
    }

    
    void removeCenterComponent() {
        if (centerComponent != null) {
            remove(centerComponent);
        }
    }
        
    protected void removeLowerComponent() {
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
        infoLabel.setText("No player loaded.");
        centerComponent = infoLabel;
        add(centerComponent, BorderLayout.CENTER);
    }
}