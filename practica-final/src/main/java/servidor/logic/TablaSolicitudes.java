package servidor.logic;

import concurrencia.Lock;
import concurrencia.LockRompeEmpate;

import java.util.*;

public class TablaSolicitudes {
    //Key: nombre fichero
    //Value: cola con nombres de receptores
    private Map<String, Queue<String>> solicitudesFichero;
    private Lock lock;

    public TablaSolicitudes() {
        solicitudesFichero = new HashMap<>();
        lock = new LockRompeEmpate(Servidor.MAX_CONCURRENT_USERS);      //TODO: Está bien este lock?
    }

    public void nuevaSolicitud(int i, String fichero, String receptor) {
        lock.takeLock(i);
        if (solicitudesFichero.get(fichero) == null) {
            solicitudesFichero.put(fichero, new ArrayDeque<String>());  //TODO: Otra implementación?
        }
        solicitudesFichero.get(fichero).add(receptor);
        lock.releaseLock(i);
    }

    public String getSiguienteReceptor(int i, String fichero) {
        String rec = null;
        lock.takeLock(i);
        try {
            rec = solicitudesFichero.get(fichero).remove();
        } catch (NoSuchElementException e) {
            //No queremos una cola vacía ocupando espacio
            solicitudesFichero.remove(fichero);
        }
        lock.releaseLock(i);
        return rec;
    }
}

