package servidor.ui;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class ServerLogger {
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_RESET = "\u001B[0m";

    //Ya es thread-safe
    private static final Logger logger = Logger.getLogger(ServerLogger.class.getName());
    private static FileHandler fileHandler;

    static {
        try {
            fileHandler = new FileHandler("serverLogfile.log");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
        } catch (IOException e) {
            System.err.println("Error al iniciar el 'logger'. No se ha podido acceder al fichero de log.");
            e.printStackTrace();
        }
    }

    public static void log(String message) {
        synchronized (logger) {
            logger.info(message);
        }
    }

    public static void logWarning(String message) {
        synchronized (logger) {
            logger.log(Level.WARNING, ANSI_YELLOW + message + ANSI_RESET);
        }
    }

    public static void logError(String message) {
        synchronized (logger) {
            logger.log(Level.SEVERE, ANSI_RED + message + ANSI_RESET);
        }
    }
}
