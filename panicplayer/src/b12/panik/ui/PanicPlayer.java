// ----------------------------------------------------------------------------
// [b12] Java Source File: PanicPlayer
//                created: 26.10.2003
//              $Revision: 1.19 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import javax.swing.*;
import javax.swing.border.LineBorder;

import b12.panik.config.Configuration;
import b12.panik.config.ConfigurationException;
import b12.panik.io.MediaIOException;
import b12.panik.player.PanicAudioPlayer;
import b12.panik.ui.util.*;
import b12.panik.util.Logging;

/**
 * Represents the main frame of the panic player.
 */
public class PanicPlayer extends JFrame {

    private static final boolean CLOSE_PLAYER_ON_EXIT = true;
    private static final Font FONT_TITLE = new Font("Arial Black", Font.BOLD, 16);
    
    private PanicAudioPlayer panicAudioPlayer;
    private JComponent playerControl;
    private Configuration conf;
    
    /** Creates a new PanicPlayer. */
    public PanicPlayer() {
        super("PanicPlayer");
        setUndecorated(true);
        // show intro window (separate thread)
		SplashScreen intro = new SplashScreen("res/munch.gif", "The PanicPlayer");
		intro.showFor(3000);
        
        panicAudioPlayer = new PanicAudioPlayer();
        
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
        
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });
        
        getRootPane().setBorder(new LineBorder(Color.BLACK));
        
        initMenuBar();
        initContent();
        
        try {
            conf = new Configuration(null);
            conf.setPlayer(panicAudioPlayer);
        } catch (ConfigurationException e1) {
            Logging.warning("Configuration could not be loaded", e1);
        }
        intro.close();
    }
    
    /** Initializes menu bar. */
    private void initMenuBar() {
        JMenuBar menuBar = new JMenuBar();
        setJMenuBar(menuBar);
        // menubar is dragger for this frame
        new Dragger(menuBar, this);
        
        // file menu
        JMenu mnFile = new MenuFile();
		mnFile.setMnemonic(KeyEvent.VK_F);
        mnFile.addPropertyChangeListener(new PropertyChangeListener() {
            public void propertyChange(PropertyChangeEvent evt) {
                final String name = evt.getPropertyName();
                if (name.equals(MenuFile.PROP_FILE_EXIT)) {
                    exitApplication();
                } else if (name.equals(MenuFile.PROP_FILE_OPEN)) {
                    loadSoundFile((File) evt.getNewValue());
                }
            }
        });
        menuBar.add(mnFile);
        
        // configuration menu
        JMenu mnConfig = new MenuConfig(conf);
        menuBar.add(mnConfig);
		mnConfig.setMnemonic(KeyEvent.VK_C);
        
        // separator
        menuBar.add(Box.createHorizontalGlue());
        
        // about menu
        JMenu aboutMenu = new MenuAbout();
        menuBar.add(aboutMenu);
        
        // exit menu
        final JLabel lblExit = new JLabel("x");
        lblExit.setBorder(BorderFactory.createEmptyBorder(0,5,0,5));
        lblExit.addMouseListener(new MouseAdapter() {
            /** @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent) */
            public void mouseClicked(MouseEvent e) {
                exitApplication();
            }
            /** @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent) */
            public void mouseEntered(MouseEvent e) {
                lblExit.setForeground(Color.WHITE);
            }
            /** @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent) */
            public void mouseExited(MouseEvent e) {
                lblExit.setForeground(Color.BLACK);
            }
            /** @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent) */
            public void mousePressed(MouseEvent e) {
                lblExit.setForeground(Color.RED);
            }
        });
        menuBar.add(lblExit);
    }

    
    /** Initializes frame content */
    private void initContent() {
        JPanel contentPane = new JPanel(new GridBagLayout());
        setContentPane(contentPane);
        contentPane.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));
        Resizer.createResizer(contentPane, this);
        
        GridBagConstraints gbc = new GridBagConstraints();
        
        // title
        JLabel lblTitle = new JLabel(getTitle());
        lblTitle.setOpaque(false);
        lblTitle.setFont(FONT_TITLE);
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        contentPane.add(lblTitle, gbc);
        
        // file opener panels
        FileOpenerPanel fopSoundFile = new FileOpenerPanel("Sound File");
        FileOpenerPanel fopConfigFile = new FileOpenerPanel("Config File");
        // TODO add listeners that load sound/config on button click
        JPanel pnlFops = new JPanel();
        pnlFops.setOpaque(false);
        pnlFops.setLayout(new BoxLayout(pnlFops, BoxLayout.Y_AXIS));
        pnlFops.add(fopSoundFile);
        pnlFops.add(fopConfigFile);
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPane.add(pnlFops, gbc);
        
        // Player Controls
        
        playerControl = panicAudioPlayer.getComponent(this);
        gbc.gridx = 3;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        contentPane.add(playerControl, gbc);
        
        // tracks panel
        TracksPanel pnlTracks = new TracksPanel();
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        contentPane.add(pnlTracks, gbc);
    }

    
    void loadSoundFile(File f) {
        try {
            conf.loadSoundFile(f);
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
        int ret = JOptionPane.showConfirmDialog(this, "Do you really want to exit?", "Panic out !!",
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
        UIUtils.center(this);
        show();
    }
}