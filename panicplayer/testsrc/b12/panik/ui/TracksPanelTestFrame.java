// ----------------------------------------------------------------------------
// [b12] Java Source File: TracksPanelTest.java
//                created: 10.01.2004
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.*;
import java.io.File;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import b12.panik.io.UrlTrack;
import b12.panik.ui.util.UIUtils;
import b12.panik.util.Logging;

/**
 * Visually tests the tracks panel.
 * @author kariem
 */
public class TracksPanelTestFrame extends JFrame {

    private JTextArea area;

    /**
     * Creates a new panel test frame.
     */
    public TracksPanelTestFrame() {
        super("Test TracksPanel");

        JPanel main = new JPanel(new BorderLayout());
        
        //
        // tracks panel
        final TracksPanel tracksPanel = new TracksPanel();
        main.add(tracksPanel, BorderLayout.CENTER);

        //
        // control panel
        JPanel controlPanel = new JPanel(new BorderLayout());
        controlPanel.setBorder(new TitledBorder("Tracksadder"));
        main.add(controlPanel, BorderLayout.WEST);

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.PAGE_AXIS));

        final JPanel pnlFile = new JPanel();
        pnlFile.setLayout(new BoxLayout(pnlFile, BoxLayout.PAGE_AXIS));
        pnlFile.setBorder(new EmptyBorder(2, 2, 1, 1));
        pnlFile.add(new JLabel("Filename"));
        final JTextField tfFileName = new JTextField();
        pnlFile.add(tfFileName);
        inputPanel.add(pnlFile);

        final JPanel pnlStart = new JPanel();
        pnlStart.setLayout(new BoxLayout(pnlStart, BoxLayout.PAGE_AXIS));
        pnlStart.setBorder(new EmptyBorder(2, 2, 1, 1));
        pnlStart.add(new JLabel("Start (in seconds)"));
        final JTextField tfStart = new JTextField();
        pnlStart.add(tfStart);
        inputPanel.add(pnlStart);
        controlPanel.add(inputPanel, BorderLayout.NORTH);

        JButton btnAdd = new JButton("Add Track");
        getRootPane().setDefaultButton(btnAdd);
        btnAdd.setAlignmentX(JButton.LEFT_ALIGNMENT);
        btnAdd.addActionListener(new ActionListener() {
            /** @see ActionListener#actionPerformed(ActionEvent) */
            public void actionPerformed(ActionEvent e) {
                String text = tfFileName.getText();
                if ((text == null) && (text.trim().length() == 0)) {
                    throwError("No Url");
                    return;
                }
                File file = new File(tfFileName.getText());
                if (file == null) {
                    throwError("File == null");
                    return;
                }
                URI uri = file.toURI();

                try {
                    long start = getLong(tfStart.getText());
                    UrlTrack track = new UrlTrack(uri, start * 1000);
                    tracksPanel.addTrack(track);
                } catch (NumberFormatException ex) {
                    throwError("Wrong Start Time", ex);
                    return;
                }
            }

            private long getLong(String string) throws NumberFormatException {
                if (string == null || string.trim().length() == 0) {
                    return 0;
                }
                return Long.parseLong(string);
            }
        });
        controlPanel.add(btnAdd, BorderLayout.SOUTH);

        //
        // error panel
        area = new JTextArea();
        area.setEditable(false);
        PrintStream ps = new PrintStream(new TextAreaOutputStream(area));
        Logging.attachLogStream(ps);

        final JScrollPane scroll = new JScrollPane(area);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBorder(new TitledBorder("Logger Output"));
        scroll.setPreferredSize(new Dimension(250, 145));
        scroll.setMinimumSize(new Dimension(10, 10));

        JSplitPane contentPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, main, scroll);
        setContentPane(contentPane);
    }

    void throwError(String message, Exception exception) {
        Logging.warning(message, exception);
    }

    void throwError(String message) {
        Logging.warning(message);
    }

    /** @see java.awt.Window#show() */
    public void show() {
        UIUtils.center(this);
        super.show();
    }

    /**
     * Main method used to execute the visual test.
     * 
     * @param args command line arguments are ignored.
     */
    public static void main(String[] args) {
        // nicer resizing
        Toolkit.getDefaultToolkit().setDynamicLayout(true);
        // more output
        Logging.setLevel(Logging.LVL_FINE);
        

        JFrame f = new TracksPanelTestFrame();
        f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosed(WindowEvent e) {
                System.exit(0);
            }
        });
        f.pack();
        f.show();
    }

    
    
    private class TextAreaOutputStream extends OutputStream {
        private JTextArea textArea;

        TextAreaOutputStream(JTextArea area) {
            this.textArea = area;
        }
        /** @see java.io.OutputStream#write(int) */
        public void write(int b) {
            write(Integer.toString(b));
        }

        /** @see java.io.OutputStream#write(byte[], int, int) */
        public void write(byte[] b, int off, int len) {
            write(new String(b, off, len));
        }

        void write(String s) {
            textArea.append(s);
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}
