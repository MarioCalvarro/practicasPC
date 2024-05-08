package cliente.ui;

import concurrencia.Lock;
import concurrencia.LockTicket;

public class ControlPrint {
    private static final Lock controladorOut = new LockTicket();

    public static void print(String message) {
        controladorOut.takeLock();
        System.out.println(message); 
        controladorOut.releaseLock();
    }

    public static void log(String message) {
        controladorOut.takeLock();
        ClienteLogger.log(message);
        controladorOut.releaseLock();
    }

    public static void logWarning(String message) {
        controladorOut.takeLock();
        ClienteLogger.logWarning(message);
        controladorOut.releaseLock();
    }

    public static void logError(String message) {
        controladorOut.takeLock();            
        ClienteLogger.logError(message);
        controladorOut.releaseLock();
    } 
}

