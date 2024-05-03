package cliente.logic;

import concurrencia.LockTicket;

import java.io.IOException;
import java.io.ObjectOutputStream;

public class ControlOutput {
    private LockTicket controladorOut;
    private ObjectOutputStream datosOut;


    public ControlOutput(ObjectOutputStream o) {
        datosOut = o;
        controladorOut = new LockTicket();
    }

    public void escribir(int id, Object obj) throws IOException {
        controladorOut.takeLock();
        datosOut.writeObject(obj);
        datosOut.flush();
        controladorOut.releaseLock();
    }

    public void cerrar() throws IOException {
        controladorOut.takeLock();
        datosOut.close();
        controladorOut.releaseLock();
    }
}
