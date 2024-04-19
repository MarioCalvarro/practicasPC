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

    public void cambioUsuario(String id, Usuario user) {
        controlador.request_write();
        datos.put(id, user);
        controlador.release_write();
    }

    public void eliminarUsuario(String id, Usuario user) {
        controlador.request_write();
        datos.remove(id);
        controlador.release_write();
    }

    public Usuario getUsuario(String id) {
        controlador.request_read();
        Usuario res = datos.get(id);
        controlador.release_read();
        return res;
    }
}
