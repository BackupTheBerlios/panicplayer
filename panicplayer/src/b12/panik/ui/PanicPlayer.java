// ----------------------------------------------------------------------------
// [b12] Java Source File: PanicPlayer.java
//                created: 26.10.2003
//              $Revision: 1.24 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.*;
import java.awt.event.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.AbstractBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;

import b12.panik.config.Configuration;
import b12.panik.config.ConfigurationException;
import b12.panik.io.MediaIOException;
import b12.panik.io.UrlTrack;
import b12.panik.player.PanicAudioPlayer;
import b12.panik.ui.util.*;
import b12.panik.util.Logging;

/**
 * Represents the main frame of the panic player.
 */
public class PanicPlayer extends JFrame {

    private static final Font FONT_TITLE = new Font("Arial Black", Font.BOLD, 16); //$NON-NLS-1$
    static final Color COLOR_BORDER = new Color(110, 110, 255);

    private PanicAudioPlayer panicAudioPlayer;
    private JComponent playerControl;
    private JComponent effectControl;
    Configuration conf;

    /** Creates a new PanicPlayer. */
    public PanicPlayer() {
        super(Messages.getString("PanicPlayer.title")); //$NON-NLS-1$
        setUndecorated(true);
        // show intro window (separate thread)
        SplashScreen intro = new SplashScreen(Messages.getString("PanicPlayer.splash.image"),
                Messages.getString("PanicPlayer.splash.text")); //$NON-NLS-1$ //$NON-NLS-2$
        intro.showFor(3000);
        panicAudioPlayer = new PanicAudioPlayer();
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (UnsupportedLookAndFeelException e) {
            String errorCause = "Unsupported Look and Feel"; //$NON-NLS-1$
            Logging.info(errorCause);
        } catch (ClassNotFoundException e) {
            String errorCause = "Look and Feel class not found"; //$NON-NLS-1$
            Logging.info(errorCause);
        } catch (InstantiationException e) {
            String errorCause = "Look and Feel InstantiationException"; //$NON-NLS-1$
            Logging.info(errorCause);
        } catch (IllegalAccessException e) {
            String errorCause = "Look and Feel Illegal Acces"; //$NON-NLS-1$
            Logging.info(errorCause);
        }
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                exitApplication();
            }
        });

        // border
        getRootPane().setBorder(new LineBorder(COLOR_BORDER, 1));

        // initialize content pane
        JPanel contentPane = new JPanel(new BorderLayout());
        Resizer.createResizer(contentPane, this);
        setContentPane(contentPane);

        initMenuBar();
        initContent();
        try {
            conf = new Configuration(null);
            conf.setPlayer(panicAudioPlayer);
        } catch (ConfigurationException e1) {
            Logging.warning("Configuration could not be loaded", e1); //$NON-NLS-1$
        }
        intro.close();
    }

    /** @see java.awt.Window#show() */
    public void show() {
        super.show();
        Dimension size = getSize();
        size.width -= 1;
        size.height -= 1;
        ((JComponent) getContentPane()).setMinimumSize(size);
    }

    /** Initializes menu bar. */
    private void initMenuBar() {
        final JMenuBar menuBar = new JMenuBar();
        menuBar.setBorderPainted(false);

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

        //
        // create title panel with the menubar and additional buttons
        final JPanel menuPanel = new JPanel() {
            /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
            public void paint(Graphics g) {
                super.paint(g);
                Rectangle bounds = this.getBounds();
                g.setColor(COLOR_BORDER);
                g.drawLine(bounds.x, bounds.y + bounds.height - 1, bounds.x + bounds.width,
                        bounds.y + bounds.height - 1);
            }
        };
        menuPanel.setLayout(new GridBagLayout());
        getContentPane().add(menuPanel, BorderLayout.NORTH);
        // menuPanel is dragger for this frame
        new Dragger(menuPanel, this);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        menuPanel.add(menuBar, gbc);

        // exit button
        final JButton btnExit = new JButton("x"); //$NON-NLS-1$
        btnExit.setFont(new Font("Arial", Font.BOLD, 12));
        btnExit.setFocusable(false);
        btnExit.setToolTipText(Messages.getString("PanicPlayer.exit.title"));
        int menuBarHeight = menuBar.getPreferredSize().height;
        btnExit.setPreferredSize(new Dimension(menuBarHeight, menuBarHeight));
        btnExit.setBorder(new AbstractBorder() {
            public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
                Graphics2D g2d = (Graphics2D) g.create();
                g2d.setColor(COLOR_BORDER);
                g2d.drawLine(x, y, x, y + height);
                g2d.drawLine(x, y + height - 1, x + width, y + height - 1);
                g2d.dispose();
            }
        });
        btnExit.addMouseListener(new ComponentMouseListener() {
            /** @see ComponentMouseListener#mouseClicked(MouseEvent) */
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                exitApplication();
            }
        });

        gbc.gridx = 1;
        gbc.weightx = 0;
        menuPanel.add(btnExit, gbc);
    }

    /** Initializes frame content */
    private void initContent() {
        // create center pane
        JPanel centerPane = new JPanel(new GridBagLayout());
        centerPane.setBorder(BorderFactory.createEmptyBorder(2, 4, 2, 4));

        // add to content pane
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.add(centerPane, BorderLayout.CENTER);

        // begin layout
        GridBagConstraints gbc = new GridBagConstraints();

        // title
        JLabel lblTitle = new JLabel(getTitle());
        lblTitle.setOpaque(false);
        lblTitle.setFont(FONT_TITLE);
        gbc.gridx = 3;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        centerPane.add(lblTitle, gbc);

        // tracks & config
        JPanel pnlFops = new JPanel();
        pnlFops.setOpaque(false);
        pnlFops.setBorder(new TitledBorder("Tracks & Config"));
        //pnlFops.setLayout(new BoxLayout(pnlFops, BoxLayout.Y_AXIS));
        pnlFops.setLayout(new GridLayout(0, 1));
        final JButton btnAddTrack = new JButton("Add Track");
        btnAddTrack.setFocusPainted(false);
        btnAddTrack.setDefaultCapable(false);
        btnAddTrack.addActionListener(new ActionListener() {
            /** @see ActionListener#actionPerformed(ActionEvent) */
            public void actionPerformed(ActionEvent e) {
                // retrieve URL of the file
                FileDialog fd = new FileDialog();
                int returnVal = fd.showOpenDialog(PanicPlayer.this);
                if (returnVal == FileDialog.APPROVE_OPTION) {
                    try {
                        URL url = fd.getSelectedFile().toURL();
                        UrlTrack track = conf.addTrack(url);
                    } catch (Exception ex) {
                        Logging.info(ex.getMessage());
                    }
                }

            }
        });
        pnlFops.add(btnAddTrack);
        final JButton btnLoadConfig = new JButton("Load Config");
        pnlFops.add(btnLoadConfig);
        btnLoadConfig.setFocusPainted(false);
        // TODO add listeners that load sound/config on button click
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridheight = 2;
        gbc.fill = GridBagConstraints.VERTICAL;
        centerPane.add(pnlFops, gbc);

        // player & player controls
        playerControl = panicAudioPlayer.getComponent(this);
        gbc.gridx = 1;
        gbc.gridwidth = 3;
        gbc.gridheight = 1;
        gbc.ipady = 50;
        gbc.fill = GridBagConstraints.BOTH;
        centerPane.add(playerControl, gbc);

        // Effect controls - tracks panel
        effectControl = panicAudioPlayer.getEffect().getComponent();
        gbc.gridy = 2;
        gbc.weightx = 1;
        gbc.weighty = 1;
        centerPane.add(effectControl, gbc);

        /*
         PSlider volSlider = new PSlider(-50, 50, 0, "Vol");
         gbc.gridx = 0;
         gbc.gridy = 2;
         gbc.gridwidth = 1;
         gbc.anchor = GridBagConstraints.CENTER;
         centerPane.add(volSlider, gbc);
         */
    }

    void loadSoundFile(File f) {
        try {
            conf.loadSoundFile(f);
        } catch (MediaIOException e) {
            String errorCause = "Problem while trying to play sound file."; //$NON-NLS-1$
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
        int ret = JOptionPane.showConfirmDialog(this, Messages
                .getString("PanicPlayer.exit.text"), Messages
                .getString("PanicPlayer.exit.title"), //$NON-NLS-1$ //$NON-NLS-2$
                JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);
        if (ret == JOptionPane.YES_OPTION) {
            if (panicAudioPlayer != null) {
                panicAudioPlayer.stop();
            }
            try {
                // save configuration
                conf.save();
            } catch (Throwable t) {
                final String errorString = Messages
                        .getString("PanicPlayer.error.confsave.title"); //$NON-NLS-1$
                JOptionPane.showMessageDialog(this, Messages
                        .getString("PanicPlayer.error.confsave.text")
                        //$NON-NLS-1$
                        + t.getMessage(), errorString, JOptionPane.ERROR_MESSAGE);
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