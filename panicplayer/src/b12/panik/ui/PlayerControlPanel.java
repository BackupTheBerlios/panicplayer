// ----------------------------------------------------------------------------
// [b12] Java Source File: PlayerControlPanel.java
//                created: 29.11.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.media.*;
import javax.swing.JPanel;

/**
 * A panel that provides controls for a simple audio player. 
 * @author kariem
 */
public class PlayerControlPanel extends JPanel {

    Player player;
    
    Component centerComponent;
    Component lowerComponent;

    /**
     * Creates a new instance of <code>PlayerControlPanel</code>.
     */
    public PlayerControlPanel(Player p) {
        super(new BorderLayout());
        this.player = p;
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
                    add(vc, BorderLayout.CENTER);
                    centerComponent = vc;
                }
                final Component cpc = player.getControlPanelComponent();
                removeLowerComponent();
                if (cpc != null) {
                    add(cpc, BorderLayout.SOUTH);
                    lowerComponent = cpc;
                }
            }
        };
        player.addControllerListener(listener);
        player.realize();
    }

    
    void removeCenterComponent() {
        if (centerComponent != null) {
            remove(centerComponent);
            validate();
        }
    }
        
    protected void removeLowerComponent() {
        if (lowerComponent != null) {
            remove(lowerComponent);
            validate();
        }
    }

    /** Removes the components from the panel that were created by the player */ 
    private void removePlayerComponents() {
        removeCenterComponent();
        removeLowerComponent();
    }
}