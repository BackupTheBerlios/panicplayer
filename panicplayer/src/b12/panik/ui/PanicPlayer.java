// ----------------------------------------------------------------------------
// [b12] Java Source File: PanicPlayer
//                created: 26.10.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

/**
 * Represents the main frame of the panic player.
 */
public class PanicPlayer extends JFrame {

    /** Creates a new PanicPlayer. */
    public PanicPlayer() {
        super("Panic Player");
    }

    /** Shows the player. */
    public void showPlayer() {
        pack();
        center();
        show();
    }

    /** Centers the frame. */
    void center() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = getSize();
        if (frameSize.height > screenSize.height) {
            frameSize.height = screenSize.height;
        }
        if (frameSize.width > screenSize.width) {
            frameSize.width = screenSize.width;
        }
        setLocation((screenSize.width - frameSize.width) / 2,
                (screenSize.height - frameSize.height) / 2);
    }
    
    // TODO closing of player should end application.
}