package servidor.logic;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Queue;

//TODO: Control concurrencia
public class TablaSolicitudes {
    //Key: nombre fichero
    //Value: cola con nombres de receptores
    private Map<String, Queue<String>> solicitudesFichero;
    
    public TablaSolicitudes() {
        solicitudesFichero = new HashMap<>();
    }

    public void nuevaSolicitud(String fichero, String receptor) {
        if (solicitudesFichero.get(fichero) == null) {
            solicitudesFichero.put(fichero, new ArrayDeque<String>());  //TODO: Otra implementación?
        }
        solicitudesFichero.get(fichero).add(receptor);
    }

    public String getSiguienteReceptor(String fichero) {
        String rec = null;
        try {
            rec = solicitudesFichero.get(fichero).remove();
        } catch (NoSuchElementException e) {
            //No queremos una cola vacía ocupando espacio
            solicitudesFichero.remove(fichero);
        }
        return rec;
    }
}

