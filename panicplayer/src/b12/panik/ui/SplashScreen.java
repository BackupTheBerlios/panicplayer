// ----------------------------------------------------------------------------
// [b12] Java Source File: SplashScreen.java
//                created: 30.11.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * The Splash Screen
 * 
 * @author schurli 
 */
public class SplashScreen extends JWindow {

    /**
	 *  Creates a SplashScreen
	 */
    public SplashScreen(String image, String text) {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        Border bd1 = BorderFactory.createBevelBorder(BevelBorder.RAISED);
        Border bd2 = BorderFactory.createEtchedBorder();
        Border bd3 = BorderFactory.createCompoundBorder(bd1, bd2);
        ((JPanel) contentPane).setBorder(bd3);
        ImageIcon icon = new ImageIcon(image);
        contentPane.add(new JLabel(" ", JLabel.CENTER), BorderLayout.NORTH);
        contentPane.add(new JLabel(icon, JLabel.CENTER), BorderLayout.CENTER);
        contentPane.add(new JLabel(text, JLabel.CENTER), BorderLayout.SOUTH);
        setContentPane(contentPane);
    }

    public void showFor(int millis) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 3, dim.height / 3);
        setSize(dim.width / 3, dim.height / 3);
        setVisible(true);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
        }
        setVisible(false);
    }
}
