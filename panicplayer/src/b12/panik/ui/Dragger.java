// ----------------------------------------------------------------------------
// [b12] Java Source File: Dragger.java
//                created: 24.12.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

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
class Dragger extends MouseAdapter implements MouseMotionListener {

    static final Color HIGHLIGHT = new Color(255, 255, 255, 50);
    
    private final JFrame frame;
    private final Glass glassPane;
    
    private Point pressPoint;
    
    static Dragger createDragger(Component component, JFrame f) {
        return new Dragger(component, f);
    }
    
    Dragger(Component component, JFrame f) {
        component.addMouseMotionListener(this);
        this.frame = f;
        glassPane = new Glass();
        frame.setGlassPane(glassPane);
        component.addMouseListener(this);
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
        glassPane.highlightComponent((Component) e.getSource());
    }
    
    /** @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent) */
    public void mouseReleased(MouseEvent e) {
        glassPane.resetHighlight();
    }
    
    /** @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent) */
    public void mouseMoved(MouseEvent e) {
        // do nothing
    }
    
    /** The glass pane that may highlight the component */
    class Glass extends JComponent {
        
        Component component;
        
        Glass() {
            setVisible(true);
        }
        
        void highlightComponent(Component comp) {
            setVisible(true);
            this.component = comp;
        }
        
        void resetHighlight() {
            setVisible(false);
            this.component = null;
        }
        
        /** @see javax.swing.JComponent#paintComponent(java.awt.Graphics) */
        protected void paintComponent(Graphics g) {
            if (component != null) {
                g.setColor(HIGHLIGHT);
                Rectangle rect = component.getBounds();
                g.fillRect(rect.x, rect.y, rect.width, rect.height);
            }
        }
    }

}