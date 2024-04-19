package servidor.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import concurrencia.ControlAcceso;
import concurrencia.Lock;
import concurrencia.MonitorRW;

public class TablaFlujos {
    private ControlAcceso controladorOut;
    private Lock controladorIn;
    private Map<String, ObjectInputStream> datosIn;
    private Map<String, ObjectOutputStream> datosOut;
    private Map<String, Integer> tablaNumLock;
    //TODO: Cambiar esto del lock
    private volatile int auxLock;

    public TablaFlujos() {
        datosIn = new HashMap<>(); 
        datosOut = new HashMap<>(); 
        controladorOut = new MonitorRW();
        auxLock = 0;
    }

    public void nuevoHilo(String id, ObjectInputStream in, ObjectOutputStream out) {
        controladorOut.request_write();
        controladorIn.takeLock(auxLock);
        datosIn.put(id, in);
        datosOut.put(id, out);
        controladorIn.realeaseLock(auxLock);
        auxLock += 1;       //TODO: Cuidado máximo nº usuarios
        controladorOut.release_write();
    }

    public void escribir(String id, Object obj) throws IOException {
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

    public Object leer(String id) throws ClassNotFoundException, IOException {
        //TODO: Cambiar lo del número
        controladorIn.takeLock(tablaNumLock.get(id));
        Object res;
        try {
            res = datosIn.get(id).readObject();
        } catch (Exception e) {
            //TODO: Error 
            controladorIn.realeaseLock(tablaNumLock.get(id));
            throw e;
        }
        controladorIn.realeaseLock(tablaNumLock.get(id));
        return res;
    }
}
