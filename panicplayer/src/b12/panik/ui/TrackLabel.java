// ----------------------------------------------------------------------------// [b12] Java Source File: TrackLabel.java//                created: 29.10.2003//              $Revision: 1.9 $// ----------------------------------------------------------------------------package b12.panik.ui;import java.awt.Color;import java.awt.event.MouseEvent;import java.awt.event.MouseMotionAdapter;import java.awt.event.MouseMotionListener;import java.text.DecimalFormat;import javax.media.Track;import javax.swing.JComponent;import javax.swing.JLabel;import javax.swing.TransferHandler;import b12.panik.io.UrlTrack;import b12.panik.ui.util.FlexibleLineBorder;/** * This class is used to represent a <code>Track</code> visually as a label. * It provides several access points to the <code>Track</code> s data. */public class TrackLabel extends JLabel {    private static final Color COLOR_BORDER = Color.GRAY;    private static final Color COLOR_BORDER_DISABLED = Color.LIGHT_GRAY;    private static final Color COLOR_CONTENT = new Color(210, 210, 200);    private static final Color COLOR_CONTENT_DISABLED = new Color(180, 180, 180);    private static final Color COLOR_FOREGROUND = Color.BLACK;    private static final Color COLOR_FOREGROUND_DISABLED = Color.GRAY;    static int pixelsPerSecond = 50;    private static final DecimalFormat FORMAT = new DecimalFormat("#.##");    private FlexibleLineBorder border;    /** The managed track. */    UrlTrack track;    /**     * Creates a new instance of <code>TrackLabel</code> with an associated     * <code>Track</code> object.     *      * @param track the track which is rendered in this object.     */    public TrackLabel(UrlTrack track) {        this(track, true);    }    /**     * Creates a new instance of <code>TrackLabel</code>.     * @param track the track.     * @param dragAndDrop whether to add the drag&drop listener;     */    TrackLabel(UrlTrack track, boolean dragAndDrop) {        setOpaque(true);        setHorizontalAlignment(JLabel.CENTER);        border = new FlexibleLineBorder(COLOR_BORDER);        setBorder(border);        // set basic visual representation        if (track != null) {            setTrack(track);        }        if (dragAndDrop) {            setTransferHandler(new TrackLabelTransferHandler());            addMouseMotionListener(new DragListener());        }    }    /**     * Sets the amount of pixels that is used per second of the rendered track     * in order to draw the <code>Track</code> in correct proportions.     *      * @param factor     *            the amount of pixels per second.     */    public static void setPixelsPerSecond(int factor) {        pixelsPerSecond = factor;    }    /**     * Returns the track.     *      * @return the track.     */    public Track getTrack() {        return track;    }    /**     * Sets the track.     *      * @param track The track.     */    public void setTrack(UrlTrack track) {        this.track = track;        updateTrack();    }    /** get properties from track and manipulate label accordingly */    private void updateTrack() {        // set enabled/disabled        setEnabled(track.isEnabled());        final double duration = track.getDuration().getSeconds();        if (duration < 1) {            border.setColor(COLOR_BORDER_DISABLED);        } else {            border.setColor(COLOR_BORDER);        }        // needs to be done        updateColors();        updateToolTip();    }    /**     * Returns the offset in pixels from the point 0 on the time line to the     * start of the track represented by this label.     * @return the x position on the time line.     */    public int getTrackStartOffset() {        return (int) (pixelsPerSecond * track.getStartTime().getSeconds());    }    /** @see java.awt.Component#setEnabled(boolean) */    public void setEnabled(boolean enabled) {        boolean oldEnabled = isEnabled();        if (oldEnabled != enabled) {            super.setEnabled(enabled);            track.setEnabled(enabled);            updateColors();            updateToolTip();        }    }    private void updateColors() {        if (isEnabled()) {            setBackground(COLOR_CONTENT);            setForeground(COLOR_FOREGROUND);        } else {            setBackground(COLOR_CONTENT_DISABLED);            setForeground(COLOR_FOREGROUND_DISABLED);        }    }    /** Set tooltip to display information according to the track */    void updateToolTip() {        String title = null;        title = track.getUri().getPath();        final int indexSlash = title.lastIndexOf('/');        if (indexSlash != -1) {            title = title.substring(indexSlash + 1);            setText(title);        }        //         StringBuffer tooltip = new StringBuffer("<html>");        // title        if (title != null) {            tooltip.append("<b>");            tooltip.append(title);            tooltip.append("</b><br>");        }        // start        tooltip.append("Start: ");        tooltip.append(format(track.getStartTime().getSeconds()));        tooltip.append("s<br>");        // duration        tooltip.append("Duration: ");        tooltip.append(format(track.getDuration().getSeconds()));        tooltip.append("s<br>");        // format        tooltip.append("Format: ");        tooltip.append(track.getFormat().getEncoding());        // disabled        if (!track.isEnabled()) {            tooltip.append("<br><em>disabled</em>");        }        // end        tooltip.append("</html>");        setToolTipText(tooltip.toString());    }    String format(double d) {        return FORMAT.format(d);    }    class DragListener extends MouseMotionAdapter implements MouseMotionListener {        /** @see MouseMotionListener#mouseDragged(MouseEvent) */        public void mouseDragged(MouseEvent e) {            e.consume();            // always copy            int action = TransferHandler.COPY;            JComponent c = (JComponent) e.getSource();            //Tell the transfer handler to initiate the drag.            TransferHandler handler = c.getTransferHandler();            handler.exportAsDrag(c, e, action);        }    }}