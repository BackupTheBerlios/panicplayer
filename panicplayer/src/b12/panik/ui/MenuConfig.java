// ----------------------------------------------------------------------------
// [b12] Java Source File: MenuConfig.java
//                created: 25.12.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import b12.panik.config.Configuration;
import b12.panik.ui.util.UIUtils;

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
        
        itemEdit = new JMenuItem("Edit Config...");
        itemEdit.setAccelerator(KeyStroke.getKeyStroke("control C"));
        itemEdit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // edit configuration
            }
       });

        itemSave = new JMenuItem("Save Config...");
        itemSave.setAccelerator(KeyStroke.getKeyStroke("control S"));
        itemSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO save configuration
            }
        });
        
        itemLoad = new JMenuItem("Load Config from File...");
        itemLoad.setAccelerator(KeyStroke.getKeyStroke("control L"));
        itemLoad.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // TODO load configuration from file
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
        Dialog dlgConf = new ConfigurationViewer();
        dlgConf.pack();
        UIUtils.centerOnParentWindow(dlgConf, this);
        dlgConf.show();
    }
}