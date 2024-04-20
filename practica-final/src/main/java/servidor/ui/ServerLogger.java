package servidor.ui;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerLogger {
    private static final Logger logger = Logger.getLogger(ServerLogger.class.getName());
    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler("logfile.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            //TODO: Error
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        synchronized (logger) {
            logger.info(message);
            System.out.println(message);
        }
    }

    public static void logWarning(String message) {
        synchronized (logger) {
            logger.log(Level.WARNING, message);
            System.out.println(message);
        }
    }

    public static void logError(String message) {
        synchronized (logger) {
            logger.log(Level.SEVERE, message);
            System.out.println(message);
        }
    }
}
