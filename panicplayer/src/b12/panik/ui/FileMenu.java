// ----------------------------------------------------------------------------
// [b12] Java Source File: FileMenu.java
//                created: 29.11.2003
//              $Revision: 1.1 $
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
 * @author kariem
 */
public class FileMenu extends JMenu {

    public static final String PROP_FILE_OPEN = "fileopen";
    
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

        itemClose = new JMenuItem("Close PanicPlayer");
        itemClose.setAccelerator(KeyStroke.getKeyStroke("control Q"));
        
        add(itemOpen);
        addSeparator();
        add(itemClose);
    }
    
    void openFile()
    {
        FileDialog fd = new FileDialog();
        
        Logging.fine(getClass().getName(), "openFile()", "dialog opened");
        int returnVal = fd.showOpenDialog(this);
        switch (returnVal)
        {
            case FileDialog.APPROVE_OPTION:
                openFile(fd.getSelectedFile());
                break;
            default:
                Logging.fine(getClass().getName(), "openFile()", "dialog closed");
        }
    }
    
    void openFile(File f)
    {
        Logging.fine(getClass().getName(), "openFile()", "file opened: " + f);
        firePropertyChange(PROP_FILE_OPEN, null, f);
    }
}
