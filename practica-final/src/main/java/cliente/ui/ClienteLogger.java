package cliente.ui;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteLogger {
    private static final Logger logger = Logger.getLogger(ClienteLogger.class.getName());

    public static void log(String message) {
        synchronized (logger) {
            logger.info(message);
        }
    }

    public static void logWarning(String message) {
        synchronized (logger) {
            logger.log(Level.WARNING, message);
        }
    }

    public static void logError(String message) {
        synchronized (logger) {
            logger.log(Level.SEVERE, message);
        }
    }
}
