package servidor.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import concurrencia.ControlAcceso;
import concurrencia.Entero;
import concurrencia.Lock;
import concurrencia.MonitorRW;

public class TablaFlujos {
    private ControlAcceso controladorOut;
    private Lock controladorIn;
    private Map<String, ObjectInputStream> datosIn;
    private Map<String, ObjectOutputStream> datosOut;
    private Map<String, Integer> tablaNumLock;

    public TablaFlujos() {
        datosIn = new HashMap<>(); 
        datosOut = new HashMap<>(); 
        controladorOut = new MonitorRW();
    }

    public void nuevoHilo(int numThread, String id, ObjectInputStream in, ObjectOutputStream out) {
        controladorOut.request_write();
        controladorIn.takeLock(numThread);
        datosIn.put(id, in);
        datosOut.put(id, out);
        controladorIn.realeaseLock(numThread);
        controladorOut.release_write();
    }

    public void escribir(String id, Object obj) {
        controladorOut.request_write();
        try {
            datosOut.get(id).writeObject(obj);
        } catch (IOException e) {
            //TODO: Error
            controladorOut.release_write();
            throw e;
        }
        datosOut.get(id).flush();
        controladorOut.release_write();
    }

    public Object leer(int numThread, String id) throws ClassNotFoundException, IOException {
        controladorIn.takeLock(numThread);
        Object res;
        try {
            res = datosIn.get(id).readObject();
        } catch (Exception e) {
            //TODO: Error 
            controladorIn.realeaseLock(tablaNumLock.get(id));
            throw e;
        }
        controladorIn.realeaseLock(numThread);
        return res;
    }
}
