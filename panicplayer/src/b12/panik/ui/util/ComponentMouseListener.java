// ----------------------------------------------------------------------------
// [b12] Java Source File: ComponentMouseListener.java
//                created: 01.01.2004
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui.util;

import java.awt.Color;
import java.awt.Component;
import java.awt.SystemColor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Simple mouse listener that may be used to highlight a component on certain
 * events. Current implementation includes "mouse over" "mouse down"
 * "mouse out". In order to override the default behaviour override the methods
 * {@linkplain #colorsOver()}, {@linkplain #colorsPressed()}, {@linkplain 
 * #colorsOut()}.
 * 
 * @author kariem
 */
public class ComponentMouseListener extends MouseAdapter {
   private boolean buttonDown;
   private Component component;

   /** @see java.awt.event.MouseAdapter#mouseClicked(java.awt.event.MouseEvent) */
   public void mouseClicked(MouseEvent e) {
      this.component = e.getComponent();
      // override this
   }

   /** @see java.awt.event.MouseAdapter#mouseEntered(java.awt.event.MouseEvent) */
   public void mouseEntered(MouseEvent e) {
      this.component = e.getComponent();
      if (buttonDown) {
         colorsPressed();
      } else {
         colorsOver();
      }
   }

   /** @see java.awt.event.MouseAdapter#mouseExited(java.awt.event.MouseEvent) */
   public void mouseExited(MouseEvent e) {
      this.component = e.getComponent();
      colorsOut();
   }

   /** @see java.awt.event.MouseAdapter#mouseReleased(java.awt.event.MouseEvent) */
   public void mouseReleased(MouseEvent e) {
      this.component = e.getComponent();
      buttonDown = false;
   }

   /** @see java.awt.event.MouseAdapter#mousePressed(java.awt.event.MouseEvent) */
   public void mousePressed(MouseEvent e) {
      this.component = e.getComponent();
      buttonDown = true;
      colorsPressed();
   }

   /**
    * Override this method to change the display for the component if the mouse
    * is over this component.
    */
   protected void colorsOver() {
      component.setBackground(Color.GRAY);
   }

   /**
    * Override this method to change the display for the component if the mouse
    * is pressed on the component.
    */
   protected void colorsPressed() {
      component.setBackground(Color.DARK_GRAY);
      component.setForeground(Color.WHITE);
   }

   /**
    * Override this method to change the display for the component if the mouse
    * has left the bounds of the component.
    */
   protected void colorsOut() {
      component.setForeground(Color.BLACK);
      component.setBackground(SystemColor.control);
   }
}