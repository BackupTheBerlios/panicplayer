// ----------------------------------------------------------------------------
// [b12] Java Source File: SplashScreen.java
//                created: 30.11.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

/**
 * The Splash Screen
 * 
 * @author schurli 
 */
public class SplashScreen extends JWindow {

    final SleeperThread sleeper;
    boolean closed = false;
    
    public SplashScreen(String image, String text) {
        JPanel contentPane = new JPanel();
        contentPane.setLayout(new BorderLayout());
        Border bd1 = BorderFactory.createBevelBorder(BevelBorder.RAISED);
        Border bd2 = BorderFactory.createEtchedBorder();
        Border bd3 = BorderFactory.createCompoundBorder(bd1, bd2);
        contentPane.setBorder(bd3);
        ImageIcon icon = new ImageIcon(image);
        contentPane.add(new JLabel(" ", JLabel.CENTER), BorderLayout.NORTH);
        contentPane.add(new JLabel(icon, JLabel.CENTER), BorderLayout.CENTER);
        contentPane.add(new JLabel(text, JLabel.CENTER), BorderLayout.SOUTH);
        setContentPane(contentPane);
        sleeper = new SleeperThread();
        
        // closes the dialog on click
        addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                close();
            }
        });
    }

    public void showFor(int millis) {
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width / 3, dim.height / 3);
        setSize(dim.width / 3, dim.height / 3);
        setVisible(true);
        sleeper.setMillis(millis);
        sleeper.start();
    }
    
    /** Closes this dialog */
    public void close() {
        if (!closed) {
            closed = true;
            setVisible(false);
            dispose();
        }
    }

    /** Sleeps and closes the dialog after some time.  */
    class SleeperThread extends Thread {
        long millis;
        
        void setMillis(int millis) {
            this.millis = millis;
        }
        
        /** @see java.lang.Thread#run() */
        public void run() {
            if (Thread.currentThread() == this) {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            close();
        }
    }
}