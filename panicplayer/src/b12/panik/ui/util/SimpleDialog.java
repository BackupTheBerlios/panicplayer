// ----------------------------------------------------------------------------
// [b12] Java Source File: SimpleDialog.java
//                created: 16.01.2004
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
 * A simple dialog intended to be subclassed
 * @author kariem
 */
public class SimpleDialog extends JDialog {

    /**
     * Creates a new instance of <code>SimpleDialog</code>.
     * @param title the title 
     */
    public SimpleDialog(String title) {
        setTitle(title);
        setModal(true);
        
        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // top
        Component top = createTopComponent();
        if (top != null) {
            contentPane.add(top, BorderLayout.CENTER);
        }

        // center
        Component center = createCenterComponent();
        if (center != null) {
            contentPane.add(center, BorderLayout.CENTER);
        }

        // OK, Cancel
        ActionListener closer = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        JButton btnClose = new JButton("Close");
        btnClose.addActionListener(closer);
        JPanel pnlControlButtons = new JPanel();
        pnlControlButtons.add(btnClose);
        contentPane.add(pnlControlButtons, BorderLayout.SOUTH);
    }
    
    /**
     * Should be overridden by subclasses in order to supply a top component.
     * @return the top component.
     */
    private Component createTopComponent() {
        return null;
    }

    /**
     * Should be overridden by subclasses in order to supply a center component.
     * 
     * @return the center component.
     */
    protected Component createCenterComponent() {
        return null;
    }
}
