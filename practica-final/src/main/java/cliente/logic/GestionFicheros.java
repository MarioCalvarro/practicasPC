package cliente.logic;

import concurrencia.ControlAcceso;
import concurrencia.MonitorRW;

import java.util.Set;

public class GestionFicheros {
    private ControlAcceso controlador;
    private Set<String> ficheros;

    public GestionFicheros(Set<String> ficheros) {
        controlador = new MonitorRW();
        this.ficheros = ficheros;

    }

    public boolean comprobarExistencia(String s) throws InterruptedException {
        controlador.request_read();
        boolean b = ficheros.contains(s);
        controlador.release_read();
        return b;
    }

    public void addFichero(String s) throws InterruptedException {
        controlador.request_read();
        ficheros.add(s);
        controlador.release_read();
    }
}
