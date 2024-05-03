package cliente.logic;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.logging.Level;

import concurrencia.LockTicket;
import servidor.ui.ServerLogger;

public class ControlPrint {
    private static final int NUM_LOCK = 3; //TODO
    private LockTicket controladorOut;


    public ControlPrint() {
        controladorOut = new LockTicket(NUM_LOCK);
    }

    public void print(int id, String message) throws IOException {
        controladorOut.takeLock(id);
        System.out.println(message); 
        controladorOut.releaseLock(id);
    }
   public  void log(int id, String message) {
        controladorOut.takeLock(id);
        ServerLogger.log(message);
        controladorOut.releaseLock(id);
    }

    public  void logWarning(int id, String message) {
        controladorOut.takeLock(id);
        ServerLogger.logWarning(message);
        controladorOut.releaseLock(id);
    }

    public  void logError(int id, String message) {
        controladorOut.takeLock(id);            
        ServerLogger.logError(message);
        controladorOut.releaseLock(id);
    } 
}

