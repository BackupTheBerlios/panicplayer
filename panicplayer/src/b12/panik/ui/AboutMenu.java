// ----------------------------------------------------------------------------
// [b12] Java Source File: AboutMenu.java
//                created: 30.11.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

/**
 * The about menu
 * 
 * @author schurli
 */
public class AboutMenu extends JMenu {

    private JMenuItem itemAbout;
    /**
	 *  
	 */
    public AboutMenu() {
        super("?");
        itemAbout = new JMenuItem("About PanicPlayer");
        itemAbout.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                about();
            }
        });
    add(itemAbout);
    }
    /**
     * 
     */
    protected void about() {
        JLabel jl = new JLabel("About");
        
    }

}
