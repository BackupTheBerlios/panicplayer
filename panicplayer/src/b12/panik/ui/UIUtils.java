// ----------------------------------------------------------------------------
// [b12] Java Source File: UIUtils.java
//                created: 29.11.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.Component;

import javax.swing.JOptionPane;

/**
 * Provides utility methods for UI components.
 * @author kariem
 */
public class UIUtils {
    
    
    private UIUtils() {
        // empty constructor for utility class.
    }

    /**
     * Opens an exception dialog and displays information about the cause.
     * 
     * @param parent the parent component.
     * @param t the throwable.
     * @param message the message.
     * @param title the title.
     */
    static final void openExceptionDialog(Component parent, Throwable t, String message, String title) {
        // TODO show throwable.
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Opens an exception dialog and displays information about the cause.
     * 
     * @param parent the parent component.
     * @param t the throwable.
     * @param message the message.
     */
    static final void openExceptionDialog(Component parent, Throwable t, String message) {
        openExceptionDialog(parent, t, message, "An error has occured.");
    }

    /**
     * Opens an exception dialog and displays information about the cause.
     * 
     * @param parent the parent component.
     * @param t the throwable.
     */
    static final void openExceptionDialog(Component parent, Throwable t) {
        openExceptionDialog(parent, t, t.getMessage());
    }
}