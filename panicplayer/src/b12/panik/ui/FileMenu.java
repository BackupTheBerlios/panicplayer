// ----------------------------------------------------------------------------
// [b12] Java Source File: FileMenu.java
//                created: 29.11.2003
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import b12.panik.util.Logging;

/**
 * The file menu.
 * 
 * @author kariem
 */
public class FileMenu extends JMenu {

    /** property for file opening.  */
    public static final String PROP_FILE_OPEN = "fileopen";
    /** property for application exit. */
    public static final String PROP_FILE_EXIT = "fileexit";

    private JMenuItem itemOpen;
    private JMenuItem itemClose;

    /**
	 * Creates a new instance of <code>FileMenu</code>.
	 */
    public FileMenu() {
        super("File");

        itemOpen = new JMenuItem("Open File");
        itemOpen.setAccelerator(KeyStroke.getKeyStroke("control O"));
        itemOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });

        itemClose = new JMenuItem("Quit");
        itemClose.setAccelerator(KeyStroke.getKeyStroke("control Q"));
        itemClose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit();
            }
        });

        add(itemOpen);
        addSeparator();
        add(itemClose);
    }

    /**
     * 
     */
    protected void exit() {
        Logging.fine(getClass().getName(), "exitFile()", "file exit");
        firePropertyChange(PROP_FILE_EXIT, null, null);
        
    }

    void openFile() {
        FileDialog fd = new FileDialog();

        Logging.fine(getClass().getName(), "openFile()", "dialog opened");
        int returnVal = fd.showOpenDialog(this);
        switch (returnVal) {
            case FileDialog.APPROVE_OPTION :
                openFile(fd.getSelectedFile());
                break;
            default :
                Logging.fine(getClass().getName(), "openFile()", "dialog closed");
        }
    }

    void openFile(File f) {
        Logging.fine(getClass().getName(), "openFile()", "file opened: " + f);
        firePropertyChange(PROP_FILE_OPEN, null, f);
    }
}