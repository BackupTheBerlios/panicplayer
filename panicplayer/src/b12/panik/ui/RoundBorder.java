// ----------------------------------------------------------------------------
// [b12] Java Source File: RoundBorder.java
//                created: 22.12.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.*;

import javax.swing.border.LineBorder;

/**
 * A border with rounded corners.
 * 
 * @author kariem
 */
class RoundBorder extends LineBorder {

    private static final int ARC_WIDTH_HEIGHT = 20;

    private static final int STROKE_WIDTH = 2;
    private static final int STROKE_HALF = STROKE_WIDTH / 2;
    private static final BasicStroke STROKE = new BasicStroke(STROKE_WIDTH);

    private static final int FONT_SIZE = 12;
    private static final Font FONT = new Font("SansSerif", Font.BOLD, FONT_SIZE);
    private static float baseline;

    private String title;
    private Insets insets;

    /**
     * Creates a new instance of <code>RoundBorder</code>.
     * 
     * @param title
     *            the title
     */
    public RoundBorder(String title) {
        this(title, null);
        this.title = title;
    }

    /**
     * Creates a new instance of <code>RoundBorder</code>.
     * 
     * @param title the title.
     * @param insets the insets.
     */
    public RoundBorder(String title, Insets insets) {
        super(Color.BLACK, 1);
        this.title = title;
        this.insets = insets;
    }

    /**
     * Paints the border for the specified component with the 
     * specified position and size. Additionally, if insets were set in the
     * constructor, their values adjust the border.
     * 
     * @param c the component for which this border is being painted
     * @param g the paint graphics
     * @param x the x position of the painted border
     * @param y the y position of the painted border
     * @param width the width of the painted border
     * @param height the height of the painted border
     */
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2d = (Graphics2D) g.create();
        if (insets != null) {
            // adjust border to insets
            x += insets.left;
            y += insets.top;
            width -= insets.left + insets.right;
            height -= insets.top + insets.bottom;
        }

        if (baseline == 0) {
            baseline = FONT.getLineMetrics(title, g2d.getFontRenderContext()).getAscent();
        }

        g2d.setColor(lineColor);
        g2d.setStroke(STROKE);
        g2d.setFont(FONT);

        // draw border
        g2d.drawRoundRect(x + STROKE_HALF, y + STROKE_HALF, width - STROKE_WIDTH, height
                - STROKE_WIDTH, ARC_WIDTH_HEIGHT, ARC_WIDTH_HEIGHT);
        // draw title
        g2d.drawString(title, x + STROKE_WIDTH + ARC_WIDTH_HEIGHT / 4, y + STROKE_WIDTH + baseline + ARC_WIDTH_HEIGHT / 4);

        // dispose graphics
        g2d.dispose();
    }

    void paintInterior(Graphics g, int x, int y, int width, int height) {
        if (insets != null) {
            // adjust border to insets
            x += insets.left;
            y += insets.top;
            width -= insets.left + insets.right;
            height -= insets.top + insets.bottom;
        }
        // draw interior of border border
        g.fillRoundRect(x + STROKE_WIDTH - 1, y + STROKE_WIDTH - 1,
                width - STROKE_WIDTH, height - STROKE_WIDTH,
                ARC_WIDTH_HEIGHT, ARC_WIDTH_HEIGHT + 2);
    }
}
