// ----------------------------------------------------------------------------
// [b12] Java Source File: PanicPlayer
//                created: 26.10.2003
//              $Revision: 1.12 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.media.Player;
import javax.swing.*;

import b12.panik.config.Configuration;
import b12.panik.config.ConfigurationException;
import b12.panik.config.InputProperty;
import b12.panik.io.MediaIOException;
import b12.panik.io.MediaInput;
import b12.panik.player.PanicAudioPlayer;
import b12.panik.util.Logging;

/**
 * Represents the main frame of the panic player.
 */
public class PanicPlayer extends JFrame {

    private static final boolean CLOSE_PLAYER_ON_EXIT = true;

    private PanicAudioPlayer panicAudioPlayer;
    private PlayerControlPanel panelPlayerControl;
    private Configuration conf;
    
    MediaInput input = new MediaInput();

    private JMenuBar menuBar;
    private FileMenu fileMenu;
    private AboutMenu aboutMenu;

    /** Creates a new PanicPlayer. */
    public PanicPlayer() {
        super("Panic Player");
		SplashScreen intro = new SplashScreen("res/munch.gif", "The PanicPlayer");
		intro.showFor(3000);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            String errorCause = "Unsupported Look and Feel";
            Logging.info(errorCause);
        } catch (ClassNotFoundException e) {
            String errorCause = "Look and Feel class not found";
            Logging.info(errorCause);
        } catch (InstantiationException e) {
            String errorCause = "Look and Feel InstantiationException";
            Logging.info(errorCause);
        } catch (IllegalAccessException e) {
            String errorCause = "Look and Feel Illegal Acces";
            Logging.info(errorCause);
        }
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        // MenuBar
        menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        fileMenu = new FileMenu();
        menuBar.add(fileMenu);
        fileMenu.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final String name = evt.getPropertyName();
                if (name.equals(FileMenu.PROP_FILE_EXIT)) {
                    exitApplication();
                } else if (name.equals(FileMenu.PROP_FILE_OPEN)) {
                    loadSoundFile((File) evt.getNewValue());
                }
            }
        });
        menuBar.add(Box.createHorizontalGlue());
        aboutMenu = new AboutMenu();
        menuBar.add(aboutMenu);
        
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        // Player Controls
        panelPlayerControl = new PlayerControlPanel(null);
        panelPlayerControl.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final String name = evt.getPropertyName();
                if (name.equals(PlayerControlPanel.PLAYER_REALIZED)) {
                    // validate component
                    PanicPlayer.this.validate();
                }
            }
        });
        contentPane.add(panelPlayerControl, BorderLayout.CENTER);
        
        try {
            conf = new Configuration(null);
        } catch (ConfigurationException e1) {
            Logging.warning("Configuration could not be loaded", e1);
        }
        intro.close();
    }

    protected void loadSoundFile(File f) {
        Player player;
        try {
            conf.setInputProperty(new InputProperty(f, 0));
            player = input.read(f);
            panelPlayerControl.setPlayer(player);
        } catch (MediaIOException e) {
            String errorCause = "Problem while trying to play sound file.";
            Logging.info(errorCause);
            UIUtils.openExceptionDialog(this, e, errorCause);
        } catch (Throwable t) {
            // the rest should be handled in a general way.
            String errorCause = t.getMessage();
            Logging.info(errorCause);
            UIUtils.openExceptionDialog(this, t, errorCause);
        }
    }

    /** Exits from the application */
    protected void exitApplication() {
        final JDialog wnd = new JDialog();

        int ret = JOptionPane.showConfirmDialog(wnd, "Do you really want to exit", "Quit",
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (ret == JOptionPane.YES_OPTION) {
            if (CLOSE_PLAYER_ON_EXIT) {
                if (panicAudioPlayer != null) {
                    panicAudioPlayer.stop();
                }
            }
            try {
                // save configuration
                conf.save();
            } catch (Throwable t) {
                final String errorString = "Error while saving configuration";
                JOptionPane.showMessageDialog(this,
                        "An error occurred, while trying to save the configuration:\n\n" 
                        + t.getMessage(),
                        errorString, JOptionPane.ERROR_MESSAGE);
                Logging.warning(errorString, t);
            }
            setVisible(false);
            dispose();
            System.exit(0);
        }
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
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }
}