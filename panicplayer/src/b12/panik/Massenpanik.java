// ----------------------------------------------------------------------------
// [b12] Java Source File: Massenpanik.java
//                created: 26.10.2003
//              $Revision: 1.5 $
// ----------------------------------------------------------------------------
package b12.panik;

import b12.panik.ui.PanicPlayer;
import b12.panik.util.Logging;

/**
 * The main entry to the program.
 */
public class Massenpanik {

    private static final String LOGFILE = "panicplayer.log";

    /**
     * The main method
     *
     * @param args
     *            command line arguments. The syntax is as follows
     *
     * <pre>
     *  &gt;program name&lt; [config.xml]
     * </pre>
     *
     * where <i>config.xml</i> indicates the configuration file to
     *            be read. If omitted the default configuration or the last
     *            user entered configuration is loaded.
     */
    public static void main(String[] args) {
        // TODO read command line arguments

        // Start logging in user home
        Logging.setLogFile(System.getProperty("user.home")
                + System.getProperty("file.separator") + LOGFILE);

        // launch player UI
        PanicPlayer pp = new PanicPlayer();
        pp.showPlayer();
    }
}