// ----------------------------------------------------------------------------
// [b12] Java Source File: MenuAbout.java
//                created: 30.11.2003
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import b12.panik.ui.util.UIUtils;
import b12.panik.util.Logging;

/**
 * The about menu
 *
 * @author schurli
 */
public class MenuAbout extends JMenu {

    /** Creates a new AboutMenu */
    public MenuAbout() {
        super("?");
        
        // log messages
        final JMenuItem itemLogFile = new JMenuItem("Messages...");
        itemLogFile.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                showLogFile();
            }
        });
        add(itemLogFile);
        
        // separator
        add(new JSeparator());
        
        // about
        final JMenuItem itemAbout = new JMenuItem("About PanicPlayer");
        itemAbout.addActionListener(new ActionListener() {
            /** @see ActionListener#actionPerformed(ActionEvent) */
            public void actionPerformed(ActionEvent e) {
                about();
            }
        });
        add(itemAbout);
    }

    /** Shows the log file output */
    void showLogFile() {
        LogFileDialog dialog = new LogFileDialog();
        UIUtils.centerOnParentWindow(dialog, this);
        dialog.show();

    }

    /** Shows the about Window */
    void about() {
        SplashScreen about = new SplashScreen("res/about.gif",
                "PanicPlayer Project by Kariem, Georg & Oliver");
        about.showUntilClick();
    }

    class LogFileDialog extends JDialog {

        LogFileDialog() {
            setTitle("Messages");
            setModal(true);
            
            JPanel contentPane = new JPanel(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            
            setContentPane(contentPane);
            
            URL logfile = Logging.getLogFile();
            
            // top information
            JLabel lblTitle = new JLabel("The following messages have been recorded from");
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.anchor = GridBagConstraints.WEST;
            contentPane.add(lblTitle, gbc);

            JTextArea textArea = new JTextArea(logfile.toString());
            textArea.setFont(IConstants.FONT_SANS_SMALL);
            textArea.setEditable(false);
            textArea.setBackground(contentPane.getBackground());
            gbc.gridwidth = 1;
            gbc.insets = new Insets(0, 5, 0, 0);
            gbc.gridy++;
            contentPane.add(textArea, gbc);
            
            // text area in scroll pane
            JTextPane area = new JTextPane();
            area.setEditable(false);
            try {
                area.setPage(logfile);
            } catch (IOException e) {
                StringBuffer text = new StringBuffer();
                text.append("Log file could not be found.\n\n");
                text.append("The following error occured:\n");
                StackTraceElement[] traces = e.getStackTrace();
                for (int i = 0; i < traces.length; i++) {
                    for (int j =0; j < i;i++) {
                        // indentation
                        text.append("  ");
                    }
                    text.append(traces[i].toString());
                    text.append('\n');
                }
                area.setText(text.toString());
            }
            
            JScrollPane scroll = new JScrollPane(area);
            scroll.setVerticalScrollBarPolicy(
                    JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
            scroll.setPreferredSize(new Dimension(250, 145));
            scroll.setMinimumSize(new Dimension(10, 10));
            scroll.setBorder(new TitledBorder("Log"));
            
            gbc.fill = GridBagConstraints.BOTH;
            gbc.gridy++;
            gbc.gridx = 0;
            gbc.weightx = 1;
            gbc.weighty = 1;
            gbc.insets = new Insets(0, 0, 0, 0);
            contentPane.add(scroll, gbc);
            
            
            // close button
            JButton btnClose = new JButton("Close");
            btnClose.addActionListener(new ActionListener() {
                /** @see ActionListener#actionPerformed(ActionEvent) */
                public void actionPerformed(ActionEvent e) {
                    dispose();
                }
            });
            gbc.weightx = 0;
            gbc.weighty = 0;
            gbc.gridy++;
            gbc.fill = GridBagConstraints.NONE;
            gbc.anchor = GridBagConstraints.CENTER;
            contentPane.add(btnClose, gbc);
            
            Dimension size = contentPane.getPreferredSize();
            size.width = Math.max(size.width, textArea.getPreferredSize().width + 20);
            size.height = Math.max(size.height, 400);
            contentPane.setPreferredSize(size);
            
            pack();
        }
        
        /** @see java.awt.Dialog#show() */
        public void show() {
            super.show();
        }
    }
}
