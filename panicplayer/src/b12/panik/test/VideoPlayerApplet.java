// ----------------------------------------------------------------------------
// [b12] Java Source File: VideoPlayerApplet.java
//                created: 20.10.2003
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------

package b12.panik.test;

import java.applet.Applet;

import javax.media.*;

/**
 * @author kariem
 */
public class VideoPlayerApplet extends Applet implements ControllerListener {
    Player p = null;
    public void init() {
        try {
            p = Manager.createPlayer(new MediaLocator("argument"));//< MediaLocator >);
            p.addControllerListener(this);
        } catch (Exception e) {
        }
    }
    public void start() {
        if (p != null)
            p.start();
    }
    public void stop() {
        if (p != null) {
            p.stop();
            p.deallocate();
        }
    }
    public synchronized void controllerUpdate(ControllerEvent e) {
        if (e instanceof RealizeCompleteEvent) {
            add(p.getVisualComponent());
            validate();
        } else if (e instanceof EndOfMediaEvent) {
            p.stop();
        }
    }
}