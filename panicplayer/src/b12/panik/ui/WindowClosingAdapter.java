// ----------------------------------------------------------------------------
// [b12] Java Source File: WindowClosingAdapter.java
//                created: 29.11.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.ui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import b12.panik.player.PanicAudioPlayer;

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
   
   public void windowClosing(WindowEvent event) {
      
      event.getWindow().setVisible(false);
      event.getWindow().dispose();
      if (closePlayer) {
         panicAudioPlayer.stop();
        
      }
   }
}
