// ----------------------------------------------------------------------------
// [b12] Java Source File: ExceptionDialog.java
//                created: 30.11.2003
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Frame;
import java.awt.HeadlessException;

import javax.swing.JDialog;

/**
 * Simple dialog that shows an exception.
 * @author kariem
 */
public class ExceptionDialog extends JDialog {

    /**
     * Creates a new instance of <code>ExceptionDialog</code>.
     * @param owner
     * @throws java.awt.HeadlessException
     */
    public ExceptionDialog(Frame owner) throws HeadlessException {
        super(owner, false);

    }
}
