package servidor.logic;

import concurrencia.ControlAcceso;
import concurrencia.SemaforoRW;

import java.util.*;

public class TablaSolicitudes {
    //Key: nombre fichero
    //Value: cola con nombres de receptores
    private Map<String, Queue<String>> solicitudesFichero;
    private ControlAcceso control;

    public TablaSolicitudes() {
        solicitudesFichero = new HashMap<>();
        control = new SemaforoRW();
    }

    public void nuevaSolicitud(String fichero, String receptor) throws InterruptedException {
        control.request_write();
        if (solicitudesFichero.get(fichero) == null) {
            solicitudesFichero.put(fichero, new ArrayDeque<String>());
        }
        solicitudesFichero.get(fichero).add(receptor);
        control.release_write();
    }

    public String getSiguienteReceptor(String fichero) throws InterruptedException {
        String rec = null;
        control.request_write();
        try {
            rec = solicitudesFichero.get(fichero).remove();
        } catch (NoSuchElementException e) {
            //No queremos una cola vacía ocupando espacio
            solicitudesFichero.remove(fichero);
        }
        control.release_write();
        return rec;
    }
}

