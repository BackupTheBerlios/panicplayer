// ----------------------------------------------------------------------------// [b12] Java Source File: TracksPanel.java//                created: 29.10.2003//              $Revision: 1.11 $// ----------------------------------------------------------------------------package b12.panik.ui;import java.awt.*;import java.beans.PropertyChangeEvent;import java.beans.PropertyChangeListener;import java.util.ArrayList;import java.util.Collection;import javax.swing.JPanel;import b12.panik.io.UrlTrack;import b12.panik.player.TrackManager;import b12.panik.ui.util.RoundBorder;import b12.panik.util.ConstraintsException;import b12.panik.util.Logging;/** * The <code>TracksPanel</code> offers a visual control to display and alter * the tracks for the {@linkplain PanicPlayer}. */public class TracksPanel extends JPanel {    final Collection tracks;    final DragPanel dragPanel;    final DropPanel dropPanel;    private TrackManager trackManager;    /**     * Creates a new instance of <code>TracksPanel</code>.     */    public TracksPanel() {        tracks = new ArrayList();        setLayout(new GridBagLayout());        setBorder(new RoundBorder("Drop Me", new Insets(1, 1, 1, 1)));        setBackground(IConstants.COLOR_TRACKS_PANEL);        // drop panel        dropPanel = new DropPanel();        dropPanel.addPropertyChangeListener(new PropertyChangeListener() {            public void propertyChange(PropertyChangeEvent evt) {                String propname = evt.getPropertyName();                if (propname.equals(DropPanel.PROP_NEW_TRACK)) {                    UrlTrack track = (UrlTrack) evt.getNewValue();                    if (track != null) {                        trackCreated(track);                    }                }            }        });        // drag panel        dragPanel = new DragPanel();        add(dropPanel, new GridBagConstraints(0, 0, 1, 1, 1, .6, GridBagConstraints.CENTER,                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));        add(dragPanel, new GridBagConstraints(0, 1, 1, 1, 1, .4, GridBagConstraints.CENTER,                GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));    }    /** Resets the panel to its initial state. */    public void reset() {        dropPanel.reset();        dragPanel.reset();    }        /**     * Sets the panel's length in seconds. The panel will scale its children     * according to the required space.     * @param seconds the seconds which should be displayed.     */    public void setLength(double seconds) {        dropPanel.setLength(seconds);    }        /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */    protected void paintComponent(Graphics g) {        if (ui != null) {            Graphics scratch = g.create();            try {                scratch.setColor(getBackground());                ((RoundBorder) getBorder()).paintInterior(scratch, 0, 0, getWidth(),                        getHeight());            } finally {                scratch.dispose();            }        }    }    /**     * Adds a track to the list of tracks managed by this TrackPanel.     * @param track the track to add.     */    public void addTrack(UrlTrack track) {        if (!tracks.contains(track)) {            Logging.fine("New track added");            tracks.add(track);            dragPanel.addTrackLabel(new TrackLabel(track));        }    }    /**     * Sets the track manager.     * @param mgr the manager.     */    public void setTrackManager(TrackManager mgr) {        this.trackManager = mgr;            }    /**     * Informs the track manager of a newly added track.     * @param track the track.     */    protected void trackCreated(UrlTrack track) {        if (trackManager != null) {            try {                // inform of new track                trackManager.addTrack(track);            } catch (ConstraintsException e) {                Logging.warning("Track could not be added to effect.", e);            }        }    }}