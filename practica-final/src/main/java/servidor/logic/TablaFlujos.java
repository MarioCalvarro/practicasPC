package servidor.logic;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import concurrencia.ControlAcceso;
import concurrencia.MonitorRW;
import servidor.ui.ServerLogger;

public class TablaFlujos {
    private ControlAcceso controladorOut;
    private Map<String, ObjectOutputStream> datosOut;

    public TablaFlujos() {
        datosOut = new HashMap<>(); 
        controladorOut = new MonitorRW();
    }

    public void nuevoHilo(String id, ObjectOutputStream out) {
        controladorOut.request_write();
        datosOut.put(id, out);
        controladorOut.release_write();
    }

    public void escribir(String id, Object obj) {
        controladorOut.request_write();
        try {
            datosOut.get(id).writeObject(obj);
            datosOut.get(id).flush();
        } catch (IOException e) {
            ServerLogger.logError("Error al escribir un mensaje en el canal con el cliente '" + id + "'.");
        }
        controladorOut.release_write();
    }
}
