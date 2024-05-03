package cliente.logic;

import java.io.IOException;

import concurrencia.Lock;
import concurrencia.LockTicket;
import servidor.ui.ServerLogger;

public class ControlPrint {
    private final Lock controladorOut;

    public ControlPrint() {
        controladorOut = new LockTicket();
    }

    public void print(String message) throws IOException {
        controladorOut.takeLock();
        System.out.println(message); 
        controladorOut.releaseLock();
    }

    public void log(String message) {
        controladorOut.takeLock();
        ServerLogger.log(message);
        controladorOut.releaseLock();
    }

    public void logWarning(String message) {
        controladorOut.takeLock();
        ServerLogger.logWarning(message);
        controladorOut.releaseLock();
    }

    public void logError(String message) {
        controladorOut.takeLock();            
        ServerLogger.logError(message);
        controladorOut.releaseLock();
    } 
}

