// ----------------------------------------------------------------------------
// [b12] Java Source File: PanicPlayer
//                created: 26.10.2003
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

import javax.media.NoPlayerException;
import javax.swing.JFrame;
import javax.swing.JMenuBar;

import b12.panik.io.IOUtils;
import b12.panik.player.PanicAudioPlayer;
import b12.panik.util.Logging;

/**
 * Represents the main frame of the panic player.
 */
public class PanicPlayer extends JFrame {
   
   private JMenuBar menuBar;
   private FileMenu fileMenu;
   
    /** Creates a new PanicPlayer. */
    public PanicPlayer() {
        super("Panic Player");
        addWindowListener(new WindowClosingAdapter(false));
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        fileMenu = new FileMenu();
        menuBar.add(fileMenu);
        fileMenu.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                String name = evt.getPropertyName();
                if (name.equals(FileMenu.PROP_FILE_EXIT)) {
                    exitApplication();
                } else if (name.equals(FileMenu.PROP_FILE_OPEN)) {
                    playSound((File) evt.getNewValue());
                }
            }
        });
    }

    protected void playSound(File f) {
        try {
            IOUtils.playSimple(f);
        } catch (Exception e) {
            Logging.severe("Error while playing sound", e);
            e.printStackTrace();
        }
    }

    /**
     * 
     */
    protected void exitApplication() {
		setVisible(false);
		dispose();
		System.exit(0);
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
                        
            if (closePlayer) {
                panicAudioPlayer.stop();
            }
            exitApplication();
        }
    }
    
}