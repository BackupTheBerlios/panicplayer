// ----------------------------------------------------------------------------
// [b12] Java Source File: FileDialog.java
//                created: 29.11.2003
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.io.File;

import javax.swing.JFileChooser;

/**
 * A special file chooser.
 * @author kariem
 */
public class FileDialog extends JFileChooser {

    /**
     * Creates a new instance of <code>FileDialog</code>.
     */
    public FileDialog() {
        super(new File("."));
    }
    /**
     * Creates a new instance of <code>FileDialog</code>.
     * @param currentDirectory
     */
    public FileDialog(File currentDirectory) {
        super(currentDirectory);
        // TODO Auto-generated constructor stub
    }
    /**
     * Creates a new instance of <code>FileDialog</code>.
     * @param currentDirectoryPath
     */
    public FileDialog(String currentDirectoryPath) {
        super(currentDirectoryPath);
        // TODO Auto-generated constructor stub
    }
}