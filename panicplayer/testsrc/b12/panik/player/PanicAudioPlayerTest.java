// ----------------------------------------------------------------------------
// [b12] Java Source File: PanicAudioPlayerTest.java
//                created: 16.01.2004
//              $Revision: 1.1 $
// ----------------------------------------------------------------------------
package b12.panik.player;

import java.io.IOException;

import javax.media.PlugInManager;

import junit.framework.TestCase;
import b12.panik.util.Logging;

/**
 * Tests the mixeffect.
 * @author kariem
 */
public class PanicAudioPlayerTest extends TestCase {

    
    // register effect plugin
    static {
        // Name of the new plugin
        String s = new String(MixEffect.class.getName());
        // Add the new plug-in to the plug-in registry
        PlugInManager.addPlugIn(s, MixEffect.FORMATS_INPUT, MixEffect.FORMATS_OUTPUT,
                PlugInManager.CODEC);
        try {
            // Save the changes to the plug-in registry
            PlugInManager.commit();
        } catch (IOException e) {
            Logging.warning("Effect plugin could not be registered with plugin manager.", e);
        }
    }
    
    PanicAudioPlayer player;
    
    /** @see junit.framework.TestCase#setUp() */
    protected void setUp() throws Exception {
        player = new PanicAudioPlayer();
    }
    
    public void testMainTrackLoading() {
        //player.setMainTrack(url);
    }
    
}
