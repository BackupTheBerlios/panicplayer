// ----------------------------------------------------------------------------
// [b12] Java Source File: Logger.java
//                created: 26.10.2003
//              $Revision: 1.6 $
// ----------------------------------------------------------------------------
package b12.panik.util;

import java.util.logging.*;

/**
 * <p>
 * In order to log all events and messages of the application, this class
 * provides several convenience methods. The main purpose is the logging
 * service, which uses the logging API.
 * </p>
 * Logging levels that are supported by this class:
 * <ul>
 * <li>FINE: Used for debugging; greatest detail.</li>
 * <li>CONFIG: Informational messages about configuration settings/setup.
 * </li>
 * <li>INFO: Informational runtime messages.</li>
 * <li>WARNING: Intended for warning messages.</li>
 * <li>SEVERE: The highest value; intended for extremely important messages.
 * </li>
 * </ul>
 */
public class Logging {

    private static final Logger logger = Logger.getLogger(Logger.class.getName());

    // in order to set the logging level, some levels are exposed

    /** Default logging level. */
    public static final byte LVL_INFO = 0;

    /** Fine logging level. */
    public static final byte LVL_FINE = -2;

    /** Config logging level. */
    public static final byte LVL_CONFIG = -1;

    /** Config logging level. */
    public static final byte LVL_WARNING = 1;

    /** Config logging level. */
    public static final byte LVL_SEVERE = 2;

    /**
     * Sets the log file for the application.
     *
     * @param filename the file name.
     */
    public static void setLogFile(String filename) {
        // remove previous handlers
        Handler[] handlers = logger.getHandlers();
        for (int i = 0; i < handlers.length; i++) {
            logger.removeHandler(handlers[i]);
        }

        addLogFile(filename);
    }

    /**
     * Adds a new file for logging.
     *
     * @param filename
     *            the file name.
     * @param level
     *            the log level.
     */
    public static void addLogFile(String filename, Level level) {
        try {
            FileHandler fh = new FileHandler(filename);
            fh.setFormatter(new SimpleFormatter());
            if (level != null) {
                fh.setLevel(level);
                final int newIntval = level.intValue();
                if (getLogLevel(logger).intValue() > newIntval) {
                    logger.setLevel(level);
                }
            }
            logger.addHandler(fh);
            System.out.println("Logging to " + filename);
            logger.log(Level.FINE, "Logging started");
        } catch (Exception e) {
            logger.log(Level.WARNING, "Error while trying to create default logfile.", e);
        }
    }

    private static Level getLogLevel(Logger log) {
        Level level = log.getLevel();
        if (level == null) {
            Logger parent = log.getParent();
            if (parent != null) {
                level = getLogLevel(parent);
            }
        }
        return level;
    }

    /**
     * Creates a new logfile with the given file name. The level of logging
     * will be LVL_FINE by default.
     *
     * @param filename
     *            the name of the log file.
     * @see #addLogFile(String)
     */
    public static void addLogFile(String filename) {
        addLogFile(filename, Level.FINE);
    }

    /**
     * Sets the application-wide logging level. Default is INFO.
     *
     * @param newLevel
     *            the new logging level.
     * @throws SecurityException
     *             if a security manager exists and if the caller does not have
     *             LoggingPermission("control").
     */
    public static void setLevel(byte newLevel) throws SecurityException {
        logger.setLevel(getLevel(newLevel));

        // change log level for root logger's handlers

        Handler[] handlers = Logger.getLogger("").getHandlers();
        for (int index = 0; index < handlers.length; index++) {
            handlers[index].setLevel(Level.FINE);
        }
    }

    /**
     * Write debug message to the logger.
     *
     * @param msg
     *            the message.
     */
    public static void fine(String msg) {
        logger.fine(msg);
    }

    /**
     * Write debug message to the logger with additional information.
     *
     * @param sourceClass
     *            the source class of the logging message.
     * @param sourceMethod
     *            the source method of the logging message.
     * @param msg
     *            the message to be logged.
     */
    public static void fine(String sourceClass, String sourceMethod, String msg) {
        logger.logp(getLevel(LVL_FINE), sourceClass, sourceMethod, msg);
    }

    /**
     * Write config message to the logger.
     *
     * @param msg
     *            the message.
     */
    public static void config(String msg) {
        logger.config(msg);
    }

    /**
     * Write info message to the logger.
     *
     * @param msg
     *            the message.
     */
    public static void info(String msg) {
        logger.info(msg);
    }

    /**
     * Write warning message to the logger.
     *
     * @param msg
     *            the message.
     */
    public static void warning(String msg) {
        logger.warning(msg);
    }

    /**
     * Write warning message to the logger and include throwable.
     *
     * @param msg
     *            the message.
     * @param thrown
     *            the throwable that was thrown
     */
    public static void warning(String msg, Throwable thrown) {
        logger.log(getLevel(LVL_WARNING), msg, thrown);
        thrown.printStackTrace();
    }

    /**
     * Write extremely important message to the logger.
     *
     * @param msg
     *            the message.
     */
    public static void severe(String msg) {
        logger.severe(msg);
    }

    /**
     * Write extremely important message to the logger and include throwable.
     *
     * @param msg
     *            the message.
     * @param thrown
     *            the throwable that was thrown
     */
    public static void severe(String msg, Throwable thrown) {
        logger.log(getLevel(LVL_SEVERE), msg, thrown);
    }

    /**
     * Delegate for logger.
     *
     * @param sourceClass
     *            source class.
     * @param sourceMethod
     *            source method.
     * @param thrown
     *            throwable that is thrown.
     */
    public static void throwing(String sourceClass, String sourceMethod, Throwable thrown) {
        logger.throwing(sourceClass, sourceMethod, thrown);
    }

    /**
     * Returns the <code>Level</code> associated with the given byte.
     *
     * @param lvl
     *            one of LVL_*.
     * @return the associated Level.
     */
    private static Level getLevel(byte lvl) {
        switch (lvl) {
            case LVL_FINE:
                return Level.FINE;
            case LVL_CONFIG:
                return Level.CONFIG;
            case LVL_WARNING:
                return Level.WARNING;
            case LVL_SEVERE:
                return Level.SEVERE;
            default:
                return Level.INFO;
        }
    }
}