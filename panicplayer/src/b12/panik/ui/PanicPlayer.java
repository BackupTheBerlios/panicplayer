// ----------------------------------------------------------------------------
// [b12] Java Source File: PanicPlayer
//                created: 26.10.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

import b12.panik.player.PanicAudioPlayer;

/**
 * Represents the main frame of the panic player.
 */
public class PanicPlayer extends JFrame {

    /** Creates a new PanicPlayer. */
    public PanicPlayer() {
        super("Panic Player");
        addWindowListener(new WindowClosingAdapter(false));
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
    
    /**
     * Adapter that closes a window.
     * 
     * @author schurli
     */
    class WindowClosingAdapter extends WindowAdapter {

        private boolean closePlayer;
        private PanicAudioPlayer panicAudioPlayer;
        
        /**
         * Creates a WindowClosingAdapter to close a window. 
         * If closePlayer is true the player will be closed
         */
        public WindowClosingAdapter(boolean closePlayer) {  
            this.closePlayer = closePlayer;
        }
        
        /**
         * Creates a WindowClosingAdapter to close a window.
         * The player will not be closed.
         */
        public WindowClosingAdapter() {
            this(false);
        }
        
        public void windowClosing(WindowEvent event) {
            event.getWindow().setVisible(false);
            event.getWindow().dispose();
            if (closePlayer) {
                panicAudioPlayer.stop();
            }
            System.exit(0);
        }
    }
    
}