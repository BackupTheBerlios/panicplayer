// ----------------------------------------------------------------------------
// [b12] Java Source File: Massenpanik.java
//                created: 26.10.2003
//              $Revision: 1.2 $
// ----------------------------------------------------------------------------
package b12.panik;

import java.util.Date;

import b12.panik.ui.PanicPlayer;
import b12.panik.util.Logging;

/**
 * The main entry to the program.
 */
public class Massenpanik {

    private static final String LOGFILE = "panicplayer.log";

    public static void main(String[] args) {
        // TODO read command line arguments
        
        
        // Start logging in user home
        Logging.setLogFile(System.getProperty("user.home")
                + System.getProperty("file.separator") + LOGFILE);

        // launch player UI
        PanicPlayer pf = new PanicPlayer();
        pf.showPlayer();
    }
    
    /**
     * Tests Georg(s) {@linkplain PanicPlayer#showPlayer()}.
     * @param name the name.
     * @param datum the birth date.
     * @return a string.
     * @throws Exception bei fehler
     */
    public String testGeorg(String name, Date datum) throws Exception {
        return null;
    }
}