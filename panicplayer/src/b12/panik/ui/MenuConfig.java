// ----------------------------------------------------------------------------
// [b12] Java Source File: MenuConfig.java
//                created: 25.12.2003
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Dialog;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import b12.panik.config.Configuration;
import b12.panik.config.ConfigurationException;
import b12.panik.ui.util.UIUtils;
import b12.panik.util.Logging;

/**
 * Menu for the configuration.
 * @author kariem
 */
public class MenuConfig extends JMenu {

    /** property for file opening.  */
    public static final String PROP_FILE_OPEN = "configopen";

    private JMenuItem itemShow;
    private JMenuItem itemEdit;
    private JMenuItem itemLoad;
    private JMenuItem itemSave;

    private Configuration config;

    /**
     * Creates a new instance of <code>FileMenu</code>.
     * @param config the configuration.
     */
    public MenuConfig(Configuration config) {
        super("Configuration");
        this.config = config;

        itemShow = new JMenuItem("Show Registered Plugins");
        itemShow.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showInfo();
            }
        });

        itemEdit = new JMenuItem("View Config...");
        itemEdit.setAccelerator(KeyStroke.getKeyStroke("control shift C"));
        itemEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showConfig();
            }
        });

        itemSave = new JMenuItem("Save Config...");
        itemSave.setAccelerator(KeyStroke.getKeyStroke("control S"));
        itemSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                saveConfig();
            }
        });

        itemLoad = new JMenuItem("Load Config from File...");
        itemLoad.setAccelerator(KeyStroke.getKeyStroke("control L"));
        itemLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loadConfig();
            }
        });

        add(itemShow);
        add(itemEdit);
        addSeparator();
        add(itemSave);
        add(itemLoad);
    }

    /** Opens a dialog with information about the installed plugins. */
    void showInfo() {
        Dialog dlg = new RegisteredComponentsViewer();
        dlg.pack();
        UIUtils.centerOnParentWindow(dlg, this);
        dlg.show();
    }

    /** Shows the current configuration */
    void showConfig() {
        Dialog dlg = new ConfigurationViewer();
        dlg.pack();
        UIUtils.centerOnParentWindow(dlg, this);
        dlg.show();
    }

    /** Saves the configuration to a file. */
    void saveConfig() {
        FileDialog fd = new FileDialog();
        final Window parentWindow = SwingUtilities.windowForComponent(this);
        int returnVal = fd.showSaveDialog(parentWindow);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fd.getSelectedFile();
            String title, text;
            int type;
            try {
                Configuration.getConfiguration().writeTo(file);
                title = "Configuration saved.";
                text = "The configuration was saved to: " + file;
                type = JOptionPane.INFORMATION_MESSAGE;
            } catch (IOException e) {
                title = "Error while saving configuration.";
                text = "An Error has occured while trying to write the configuration.\n "
                        + "See the error log for more information";
                type = JOptionPane.ERROR_MESSAGE;
                Logging.warning(title, e);
            }
            JOptionPane.showMessageDialog(parentWindow, text, title, type);
        }
    }

    /** Loads the configuration. */
    void loadConfig() {
        FileDialog fd = new FileDialog();
        final Window parentWindow = SwingUtilities.windowForComponent(this);
        int returnVal = fd.showOpenDialog(parentWindow);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            File file = fd.getSelectedFile();
            String title, text;
            int type;
            try {
                Configuration conf = Configuration.getConfiguration();
                conf.resetConfig();
                conf.loadConfig(file.toURI().toString());
                title = "Configuration loaded.";
                text = "The configuration was successfully loaded from " + file;
                type = JOptionPane.INFORMATION_MESSAGE;
            } catch (ConfigurationException e) {
                title = "Error while loading configuration.";
                text = "An Error has occured while trying to load the configuration.\n "
                    + "See the error log for more information";
                type = JOptionPane.ERROR_MESSAGE;
                Logging.warning(title, e);
            }
            JOptionPane.showMessageDialog(parentWindow, text, title, type);
        }
    }
}