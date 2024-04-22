package cliente.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteLogger {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";

    private static final Logger logger = Logger.getLogger(ClienteLogger.class.getName());

    public static void log(String message) {
        synchronized (logger) {
            logger.info(message);
        }
    }

    public static void logWarning(String message) {
        synchronized (logger) {
            logger.log(Level.WARNING, ANSI_YELLOW + message);
        }
    }

    public static void logError(String message) {
        synchronized (logger) {
            logger.log(Level.SEVERE, ANSI_RED + message);
        }
    }
}
