package servidor.logic;

import java.util.HashMap;
import java.util.Map;

import concurrencia.ControlAcceso;
import concurrencia.SemaforoRW;

public class BaseDatos {
    private Map<String, Usuario> datos;
    private ControlAcceso controlador;

    public BaseDatos() {
        datos = new HashMap<>(); 
        controlador = new SemaforoRW();
    }

    public void conexionUsuario(Usuario user) {
        controlador.request_write();
        String id = user.getId();
        datos.put(id, user);
        datos.get(id).conectar();
        controlador.release_write();
    }

    public void eliminarUsuario(String id, Usuario user) {
        controlador.request_write();
        datos.remove(id);
        controlador.release_write();
    }

    public void enviarUsuarios(String id, TablaFlujos tFlujos) {
        controlador.request_read();
        //TODO: Hacer
        controlador.release_read();
    }
}
