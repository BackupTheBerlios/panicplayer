// ----------------------------------------------------------------------------
// [b12] Java Source File: ConfigurationDialog.java
//                created: 25.12.2003
//              $Revision: 1.4 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.StringWriter;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import b12.panik.config.Configuration;
import b12.panik.ui.util.SimpleDialog;
import b12.panik.util.Utils;

/**
 * Allows to view the current configuration.
 * @author kariem
 */
public class ConfigurationViewer extends SimpleDialog {

    /**
     * Creates a new <code>ConfigurationDialog</code> with the given
     * configuration.
     */
    public ConfigurationViewer() {
        super("View Current Configuration");
    }
    
    /** @see b12.panik.ui.util.SimpleDialog#createCenterComponent() */
    protected Component createCenterComponent() {
        // error panel
        JTextArea area = new JTextArea();
        String text;
        StringWriter w = new StringWriter();
        try {
            Configuration.getConfiguration().writeTo(w);
            text = w.toString();
        } catch (IOException e1) {
            text = Utils.getErrorString(e1,
            "An error occurred while trying to get configuration information.");
        }
        area.setText(text);
        area.setEditable(false);

        final JScrollPane scroll = new JScrollPane(area);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBorder(new TitledBorder("Current Configuration"));
        scroll.setPreferredSize(new Dimension(250, 145));
        scroll.setMinimumSize(new Dimension(10, 10));
        return scroll;
    }
}
