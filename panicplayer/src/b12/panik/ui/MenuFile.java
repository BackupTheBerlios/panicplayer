// ----------------------------------------------------------------------------
// [b12] Java Source File: MenuFile.java
//                created: 29.11.2003
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.*;

import b12.panik.util.Logging;

/**
 * The file menu.
 *
 * @author kariem
 */
public class MenuFile extends JMenu {

    /** property for file opening.  */
    public static final String PROP_FILE_OPEN = "fileopen";
    /** property for url opening.  */
    public static final String PROP_FILE_URL = "urlopen";
    /** property for application exit. */
    public static final String PROP_FILE_EXIT = "fileexit";

    /**
     * Creates a new instance of <code>FileMenu</code>.
     */
    public MenuFile() {
        super("File");

        JMenuItem itemOpen = new JMenuItem("Load Main Track...");
        itemOpen.setAccelerator(KeyStroke.getKeyStroke("control O"));
        itemOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        JMenuItem itemUrl = new JMenuItem("Load Main Track (from URL)...");
        itemUrl.setAccelerator(KeyStroke.getKeyStroke("control shift O"));
        itemUrl.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openUrl();
            }
        });
        
        JMenuItem itemClose = new JMenuItem("Quit");
        itemClose.setAccelerator(KeyStroke.getKeyStroke("control Q"));
        itemClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        add(itemOpen);
        add(itemUrl);
        addSeparator();
        add(itemClose);
    }

    void exit() {
        Logging.fine("Clicked to exit application.");
        firePropertyChange(PROP_FILE_EXIT, null, null);
    }

    void openFile() {
        FileDialog fd = new FileDialog();
        fd.setMultiSelectionEnabled(true);

        Logging.fine("File dialog opened to load track.");
        int returnVal = fd.showOpenDialog(SwingUtilities.windowForComponent(this));
        if (returnVal == FileDialog.APPROVE_OPTION) {
            File[] files = fd.getSelectedFiles();
            openFiles(files);
        } else {
            Logging.fine("Dialog closed without selection.");
        }
    }

    private void openFiles(final File[] files) {
        if (files != null && files.length > 0) {
            Thread fileOpener = new Thread("File Opener") {
                /** @see java.lang.Thread#run() */
                public void run() {
                    for (int i = 0; i < files.length; i++) {
                        fireChange(PROP_FILE_OPEN, null, files[i]);
                        Logging.fine("File opened: " + files[i]);
                    }
                }
            };
            fileOpener.start();
        }
    }

    void openUrl() {
        String selection = JOptionPane.showInputDialog(SwingUtilities.windowForComponent(this),
                "Please enter the URL (rtp://<host>[path]).", "Select input URL",
                JOptionPane.QUESTION_MESSAGE);
     
        if (selection != null) {
            firePropertyChange(PROP_FILE_URL, null, selection);
        }
    }

    void fireChange(String prop, Object oldObj, Object newObj) {
        firePropertyChange(prop, oldObj, newObj);
    }

}