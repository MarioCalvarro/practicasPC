package servidor.logic;

import concurrencia.ControlAcceso;
import concurrencia.MonitorRW;
import servidor.ui.ServerLogger;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class TablaFlujos {
    private ControlAcceso controladorOut;
    private Map<String, ObjectOutputStream> datosOut;

    public TablaFlujos() {
        datosOut = new HashMap<>();
        controladorOut = new MonitorRW();
    }

    public void nuevoHilo(String id, ObjectOutputStream out) throws InterruptedException {
        controladorOut.request_write();
        datosOut.put(id, out);
        controladorOut.release_write();
    }

    public void escribir(String id, Object obj) throws InterruptedException {
        controladorOut.request_write();
        try {
            datosOut.get(id).writeObject(obj);
            datosOut.get(id).flush();
        } catch (IOException e) {
            ServerLogger.logError("Error al escribir un mensaje en el canal con el cliente '" + id + "'.");
        }
        controladorOut.release_write();
    }

    public void cerrar(String id) throws InterruptedException, IOException {
        controladorOut.request_write();
        datosOut.get(id).close();
        controladorOut.release_write();
    }
}
