// ----------------------------------------------------------------------------// [b12] Java Source File: TracksPanel.java//                created: 29.10.2003//              $Revision: 1.8 $// ----------------------------------------------------------------------------package b12.panik.ui;import java.awt.*;import java.util.ArrayList;import java.util.Collection;import javax.swing.JPanel;import b12.panik.io.UrlTrack;import b12.panik.ui.util.RoundBorder;import b12.panik.util.Logging;/** * The <code>TracksPanel</code> offers a visual control to display and alter * the tracks for the {@linkplain PanicPlayer}. */public class TracksPanel extends JPanel {    final Collection tracks;    final DragPanel dragPanel;    /**     * Creates a new instance of <code>TracksPanel</code>.     */    public TracksPanel() {        tracks = new ArrayList();        setLayout(new GridBagLayout());        setBorder(new RoundBorder("Drop Me", new Insets(1, 1, 1, 1)));        setBackground(IConstants.COLOR_TRACKS_PANEL);        // drop panel        DropPanel dropPanel = new DropPanel();        // drag panel        dragPanel = new DragPanel();        add(dropPanel, new GridBagConstraints(0, 0, 1, 1, 1, .6, GridBagConstraints.CENTER,                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));        add(dragPanel, new GridBagConstraints(0, 1, 1, 1, 1, .4, GridBagConstraints.CENTER,                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));        /*        DragObject dragLabel = new DragObject("DragME", DnDConstants.ACTION_COPY_OR_MOVE);        dragLabel.setBackground(Color.white);        dragLabel.setOpaque(true);        dragPanel.add(dragLabel);        // TODO initialize with default information         */    }    /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */    protected void paintComponent(Graphics g) {        if (ui != null) {            Graphics scratch = g.create();            try {                scratch.setColor(getBackground());                ((RoundBorder) getBorder()).paintInterior(scratch, 0, 0, getWidth(),                        getHeight());            } finally {                scratch.dispose();            }        }    }    /**     * Adds a track to the list of tracks managed by this TrackPanel.     * @param track the track to add.     */    public void addTrack(UrlTrack track) {        if (!tracks.contains(track)) {            Logging.fine("New track added");            tracks.add(track);            dragPanel.addTrackLabel(new TrackLabel(track));        }    }}