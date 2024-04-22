package cliente.logic;
import concurrencia.Lock;
import concurrencia.LockRompeEmpate;
import servidor.ui.ServerLogger;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class ControlOutput {
	private static final int NUM_LOCK = 3;
    private Lock controladorOut;
    private ObjectOutputStream datosOut;
    

    public ControlOutput(ObjectOutputStream o) {
        datosOut = o;
        controladorOut = new LockRompeEmpate(NUM_LOCK);
    }

    //ID: 0 hilo cliente, 1 OyenteServidor, 2 HiloReceptor
    public void escribir(int id, Object obj) throws IOException {      //TODO: Error fuera de rango?
        controladorOut.takeLock(id);
        datosOut.writeObject(obj);
        datosOut.flush();
        controladorOut.releaseLock(id);
    }
}
