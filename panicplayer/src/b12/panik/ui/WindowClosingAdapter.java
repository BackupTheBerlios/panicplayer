// ----------------------------------------------------------------------------
// [b12] Java Source File: WindowClosingAdapter.java
//                created: 29.11.2003
//              $Revision: 1.3 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import b12.panik.player.PanicAudioPlayer;

/**
 * A WindowAdapter to close a window.
 */
public class WindowClosingAdapter extends WindowAdapter {

   private boolean closePlayer;
   private PanicAudioPlayer panicAudioPlayer;
   
   /**
    * Creates a WindowClosingAdapter to close a window. 
    * If closePlayer is true the player will be closed
    */
   public WindowClosingAdapter(boolean closePlayer) {  
      this.closePlayer = closePlayer;
   }
   
   /**
    * Creates a WindowClosingAdapter to close a window.
    * The player will not be closed.
    */
   public WindowClosingAdapter() {
      this(false);
   }

   /** @see WindowListener#windowClosing(java.awt.event.WindowEvent) */
   public void windowClosing(WindowEvent event) {
      event.getWindow().setVisible(false);
      event.getWindow().dispose();
      if (closePlayer) {
         if (panicAudioPlayer != null) {
            panicAudioPlayer.stop();
         }
      }
   }
}
