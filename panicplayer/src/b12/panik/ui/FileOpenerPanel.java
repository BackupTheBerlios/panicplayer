// ----------------------------------------------------------------------------
// [b12] Java Source File: FileOpenerPanel.java
//                created: 22.12.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.*;

import javax.swing.*;

/**
 * A small panel that shows a browse button and a text field to open files.
 * @author kariem
 */
class FileOpenerPanel extends JPanel {

    private static final int NB_CHARS = 15;
    private static final int FONT_SIZE = 12;
    private static final Font FONT = new Font("SansSerif", Font.BOLD, FONT_SIZE);
    
    /**
     * Creates a new instance of <code>FileOpenerPanel</code>.
     * @param title the title.
     */
    public FileOpenerPanel(String title) {
        super(new GridBagLayout());
        setOpaque(false);
        
        // label
        JLabel lblTitle = new JLabel(title);
        lblTitle.setOpaque(false);
        lblTitle.setFont(FONT);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        add(lblTitle, gbc);
        
        
        // text field 
        JTextField tfName = new JTextField(NB_CHARS);
        gbc.gridy = 1;
        //gbc.fill = GridBagConstraints.HORIZONTAL;
        add(tfName, gbc);
        
        // "browse" button
        JButton btnBrowse = new JButton("Browse");
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(btnBrowse, gbc);
        
        Dimension preferred = getPreferredSize();
        preferred.width = tfName.getPreferredSize().width + btnBrowse.getPreferredSize().width + 10;
        setPreferredSize(preferred);
    }
}