// ----------------------------------------------------------------------------
// [b12] Java Source File: Dragger.java
//                created: 24.12.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui.util;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * A class that creates a dragger for a component. If the component is moved
 * (drag) the frame will be moved accordingly. In addition the component will
 * be highlighted.
 * 
 * @author kariem
 */
public class Dragger extends MouseAdapter implements MouseMotionListener {

    static final Color MOUSE_OVER = new Color(255, 255, 255, 50);
    static final Color MOUSE_PRESS = new Color(255, 255, 255, 128);
    
    private final JFrame frame;
    private final Glass glassPane;
    
    private Point pressPoint;
    private Component component;
    
    /**
     * Creates a new instance of <code>Dragger</code>.
     * @param component the component that will be dragged.
     * @param f the frame that will be moved.
     */
    public Dragger(Component component, JFrame f) {
        this.frame = f;
        this.component = component;
        glassPane = new Glass();
        frame.setGlassPane(glassPane);
        component.addMouseListener(this);
        component.addMouseMotionListener(this);
    }

    /** @see java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent) */
    public void mouseDragged(MouseEvent e) {
        // get drag location
        Point dragPoint = e.getPoint();
        
        // get current location
        Point location = frame.getLocation();
        
        // change location if drag moves the frame significantly
        final int difX = dragPoint.x - pressPoint.x;
        final int difY = dragPoint.y - pressPoint.y;
        
        if (Math.abs(difX) > 2 || Math.abs(difY) > 2) {
            location.translate(difX, difY);
            frame.setLocation(location);
        }
    }

    /** @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent) */
    public void mousePressed(MouseEvent e) {
        pressPoint = e.getPoint();
        glassPane.highlightComponent(component.getBounds(), MOUSE_PRESS);
        glassPane.repaint();
    }
    
    /** @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent) */
    public void mouseReleased(MouseEvent e) {
        glassPane.resetHighlight();
    }

    /** @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent) */
    public void mouseEntered(MouseEvent e) {
        glassPane.highlightComponent(component.getBounds(), MOUSE_OVER);
    }
    
    /** @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent) */
    public void mouseExited(MouseEvent e) {
        glassPane.resetHighlight();
    }
    
    /** The glass pane that may highlight the component */
    class Glass extends JComponent {
        
        private Rectangle r;
        Color color;
        
        Glass() {
            setVisible(true);
        }
        
        void highlightComponent(Rectangle rect, Color col) {
            setVisible(true);
            this.r = rect;
            this.color = col;
        }
        
        void resetHighlight() {
            setVisible(false);
            this.r = null;
            this.color = null;
        }
        
        /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
        protected void paintComponent(Graphics g) {
            if (r != null) {
                g.setColor(color);
                g.fillRect(r.x, r.y, r.width, r.height);
            }
        }
    }

    /** @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent) */
    public void mouseMoved(MouseEvent e) {
        // not implemented
    }
}