package cliente.ui;

import java.io.IOException;

import concurrencia.Lock;
import concurrencia.LockTicket;
import servidor.ui.ServerLogger;

public class ControlPrint {
    private static final Lock controladorOut = new LockTicket();

    public static void print(String message) throws IOException {
        controladorOut.takeLock();
        System.out.println(message); 
        controladorOut.releaseLock();
    }

    public static void log(String message) {
        controladorOut.takeLock();
        ServerLogger.log(message);
        controladorOut.releaseLock();
    }

    public static void logWarning(String message) {
        controladorOut.takeLock();
        ServerLogger.logWarning(message);
        controladorOut.releaseLock();
    }

    public static void logError(String message) {
        controladorOut.takeLock();            
        ServerLogger.logError(message);
        controladorOut.releaseLock();
    } 
}

