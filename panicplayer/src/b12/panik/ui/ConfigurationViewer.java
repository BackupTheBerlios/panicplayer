// ----------------------------------------------------------------------------
// [b12] Java Source File: ConfigurationDialog.java
//                created: 25.12.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.media.PlugInManager;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Allows to view the current configuration.
 * @author kariem
 */
public class ConfigurationViewer extends JDialog {

    /**
     * Creates a new <code>ConfigurationDialog</code> with the given
     * configuration.
     */
    public ConfigurationViewer() {
        super();
        setTitle("Currently Registered Plugins");
        setModal(true);

        JPanel contentPane = new JPanel(new BorderLayout());
        setContentPane(contentPane);

        // plugins
        JPanel possiblePlugins = new PluginViewer();
        possiblePlugins.setBorder(new TitledBorder("Available Plugins"));
        contentPane.add(possiblePlugins, BorderLayout.CENTER);

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

    class PluginViewer extends JPanel {
        GridBagConstraints gbc;

        PluginViewer() {
            setLayout(new GridBagLayout());

            gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.gridy = 0;

            addPlugin("Codecs:", PlugInManager.CODEC);
            addPlugin("Effects:", PlugInManager.EFFECT);
            addPlugin("DeMUX:", PlugInManager.DEMULTIPLEXER);
            addPlugin("MUX:", PlugInManager.MULTIPLEXER);
            addPlugin("Renderer:", PlugInManager.RENDERER);
        }

        private void addPlugin(String text, int type) {
            Vector v = PlugInManager.getPlugInList(null, null, type);
            addRow(new JLabel(text), v);
        }

        private void addRow(JLabel label, Vector list) {
            // add label
            gbc.gridx = 0;
            gbc.ipadx = 4;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.fill = GridBagConstraints.NONE;
            add(label, gbc);

            // add right component
            gbc.gridx = 1;
            gbc.fill = GridBagConstraints.HORIZONTAL;
            final JComboBox second;
            final JLabel third;
            if (list == null || list.isEmpty()) {
                second = new JComboBox(new String[] { "<< empty >>" });
                second.setEnabled(false);
                third = null;
            } else {
                second = new JComboBox(list);
                third = new JLabel(new String("  (" + list.size() + ")"));
            }
            add(second, gbc);

            if (third != null) {
                gbc.gridx = 2;
                gbc.anchor = GridBagConstraints.EAST;
                gbc.fill = GridBagConstraints.NONE;
                add(third, gbc);
            }

            // increment y
            gbc.gridy++;
        }
    }
}
