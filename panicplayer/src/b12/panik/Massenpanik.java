// ----------------------------------------------------------------------------
// [b12] Java Source File: Massenpanik.java
//                created: 26.10.2003
//              $Revision: 1.7 $
// ----------------------------------------------------------------------------
package b12.panik;

import java.io.File;

import b12.panik.config.Configuration;
import b12.panik.ui.PanicPlayer;
import b12.panik.util.Logging;

/**
 * The main entry to the program.
 */
public class Massenpanik {

    private static final String LOGFILE = "panicplayer.log";
    private static boolean logFileSet;

    /**
     * The main method
     *
     * @param args command line arguments. The syntax is as follows:
     *
     * <pre>
     *  &lt;program name&gt; [config.xml] [-log &lt;logfile name&gt;]
     * </pre>
     *
     * where <i>config.xml</i> indicates the configuration file to be read. If
     * omitted the default configuration is loaded.
     */
    public static void main(String[] args) {
        // read command line arguments
        if (args.length == 1) {
            // set configuration file
            Configuration.setCurrentConfFile(args[0]);
        } else if (args.length > 1) {
            if (args[0].equals("-log")) {
                // second argument is logfile
                setLog(args[1]);
            } else {
                // set configuration file
                Configuration.setCurrentConfFile(args[0]);
                if (args.length > 2 && args[1].equals("-log")) {
                    // third argument is logfile
                    setLog(args[2]);
                }
            }
        }

        if (!logFileSet) {
            // set default log file
            Logging.setLogFile(LOGFILE);
        }

        // launch player UI
        PanicPlayer pp = new PanicPlayer();
        pp.showPlayer();
    }

    static void setLog(String log) {
        Logging.setLogFile(log);
        logFileSet = true;
    }
}