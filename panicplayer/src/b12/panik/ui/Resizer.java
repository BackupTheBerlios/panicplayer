// ----------------------------------------------------------------------------
// [b12] Java Source File: Resizer.java
//                created: 24.12.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.*;
import java.awt.event.*;

import javax.swing.JPanel;

/**
 * A component that resizes a frame. 
 * @author kariem
 */
public class Resizer extends MouseAdapter implements MouseMotionListener {

    private static final byte THRESHOLD = 4;

    private static final byte BOTTOM = 1 << 1;
    private static final byte LEFT = 1 << 2;
    private static final byte RIGHT = 1 << 3;
    private byte edge;

    private Component component;
    private JPanel panel;
    Rectangle panelBounds;

    boolean resizing;

    private Point pressPoint;

    Resizer(final JPanel panel, final Component component) {
        this.panel = panel;
        this.component = component;
        this.panelBounds = panel.getBounds();

        panel.addMouseMotionListener(this);
        panel.addMouseListener(this);
        panel.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent e) {
                panelBounds = panel.getBounds();
            }
        });
    }

    /**
     * Creates a new resizer for the panel and the frame.
     * @param pane the panel.
     * @param c the component that will be resized.
     * @return a newly created resizer.
     */
    public static Resizer createResizer(JPanel pane, Component c) {
        return new Resizer(pane, c);
    }

    /** @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent) */
    public void mouseDragged(MouseEvent e) {
        if (resizing) {
            Point resizePoint = e.getPoint();

            // change location only if drag moves the frame significantly
            final int difX = resizePoint.x - pressPoint.x;
            final int difY = resizePoint.y - pressPoint.y;

            
            if (Math.abs(difX) > 2 || Math.abs(difY) > 2) {
                Rectangle compBounds = component.getBounds();
                
                int oldX = compBounds.x;
                int oldY = compBounds.y;
                
                if ((edge & BOTTOM) != 0) {
                    compBounds.height += difY;
                    if ((edge & LEFT) != 0) {
                        compBounds.x += difX;
                        compBounds.width -= difX;
                    } else if ((edge & RIGHT) != 0) {
                        compBounds.width += difX;
                    } 
                } else if ((edge & LEFT) != 0) {
                    compBounds.x += difX;
                    compBounds.width -= difX;
                } else if ((edge & RIGHT) != 0) {
                    compBounds.width += difX;
                }
                setBounds(compBounds);                        
                
                Point frameLoc = component.getLocation();
                pressPoint.x = resizePoint.x - (frameLoc.x - oldX);
                pressPoint.y = resizePoint.y - (frameLoc.y - oldY);
                
                component.validate();
            }
        }
    }

    /** Sets the bounds for the frame, respecting its minimum size. */
    private void setBounds(Rectangle r) {
        Dimension minBounds = component.getMinimumSize();
        if (r.width > minBounds.width && r.height > minBounds.height) {
            component.setBounds(r);
        }
    }

    /** @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent) */
    public void mouseMoved(MouseEvent e) {
        Point p = e.getPoint();

        // see if mouse lies within 4px of edge
        if (isBottom(panelBounds, p)) {
            if (isLeft(panelBounds, p)) {
                setCursor(BOTTOM | LEFT);
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.SW_RESIZE_CURSOR));
            } else if (isRight(panelBounds, p)) {
                setCursor(BOTTOM | RIGHT);
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
            } else {
                setCursor(BOTTOM);
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.S_RESIZE_CURSOR));
            }
        } else {
            if (isLeft(panelBounds, p)) {
                setCursor(LEFT);
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.W_RESIZE_CURSOR));
            } else if (isRight(panelBounds, p)) {
                setCursor(RIGHT);
                panel.setCursor(Cursor.getPredefinedCursor(Cursor.E_RESIZE_CURSOR));
            } else {
                resetCursor();
            }
        }
    }
    
    /** @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent) */
    public void mousePressed(MouseEvent e) {
        pressPoint = e.getPoint();
    }
    
    /** @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent) */
    public void mouseReleased(MouseEvent e) {
        pressPoint = null;
    }

    private void setCursor(int i) {
        edge = (byte) i;
        resizing = true;
    }
    
    private void resetCursor() {
        panel.setCursor(Cursor.getDefaultCursor());
        edge = 0;
        resizing = false;
    }
    
    private boolean isLeft(Rectangle r, Point p) {
        return p.x <= r.x + THRESHOLD;
    }

    private boolean isRight(Rectangle r, Point p) {
        return p.x >= r.x + r.width - THRESHOLD;
    }

    private boolean isBottom(Rectangle r, Point p) {
        return p.y >= r.height - THRESHOLD;
    }
}
