// ----------------------------------------------------------------------------
// [b12] Java Source File: UIUtils.java
//                created: 29.11.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui.util;

import java.awt.*;

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
    public static final void openExceptionDialog(Component parent, Throwable t, String message,
            String title) {
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
    public static final void openExceptionDialog(Component parent, Throwable t, String message) {
        openExceptionDialog(parent, t, message, "An error has occured.");
    }

    /**
     * Opens an exception dialog and displays information about the cause.
     * 
     * @param parent the parent component.
     * @param t the throwable.
     */
    public static final void openExceptionDialog(Component parent, Throwable t) {
        openExceptionDialog(parent, t, t.getMessage());
    }

    /** 
     * Centers the component on the screen. 
     * @param c the component to center.
     */
    public static void center(Component c) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        center(c, new Rectangle(0, 0, screenSize.width, screenSize.height));
    }

    /**
     * Centers the component on its parent.
     * @param target the target component.
     * @param parent the parent component.
     */
    public static void center(Component target, Component parent) {
        center(target, parent.getBounds());
    }

    /**
     * Centers the component on a rectangle.
     * @param target the target component.
     * @param r the rectangle.
     */
    public static void center(Component target, Rectangle r) {
        center(target, r, false);
    }

    /**
     * Centers the component on a rectangle. Size adjustment is optional.
     * @param target the target component.
     * @param r the rectangle.
     * @param adjustSize whether to adjust the components size not to be larger
     *         than the rectangle.
     */
    public static void center(Component target, Rectangle r, boolean adjustSize) {
        Dimension d = target.getSize();
        if (adjustSize) {
            if (d.height > r.height) {
                d.height = r.height;
            }
            if (d.width > r.width) {
                d.width = r.width;
            }
        }
        target.setLocation(r.x + (r.width / 2) - (d.width / 2), r.y + (r.height / 2)
                - (d.height / 2));
    }
}