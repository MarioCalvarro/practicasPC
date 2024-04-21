package servidor.logic;

import concurrencia.ControlAcceso;
import concurrencia.SemaforoRW;
import mensaje.MsjListaUsuarios;
import mensaje.TipoMensaje;
import servidor.ui.ServerLogger;

import java.util.HashMap;
import java.util.Map;

public class BaseDatos {
    private Map<String, Usuario> datos;
    private ControlAcceso controlador;

    public BaseDatos() {
        //TODO: Guardar información en un fichero
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

    public void desconexionUsuario(String id) {
        controlador.request_write();
        datos.get(id).desconectar();
        controlador.release_write();
    }

    public void nuevoFicheroEnUser(String idUser, String fichero) {
        controlador.request_write();
        datos.get(idUser).nuevoFichero(fichero);
        controlador.release_write();
    }

    public void eliminarUsuario(String id, Usuario user) {
        controlador.request_write();
        datos.remove(id);
        controlador.release_write();
    }

    public void enviarUsuarios(String id, TablaFlujos flujos) {
        controlador.request_read();
        try {
            flujos.escribir(id, new MsjListaUsuarios(TipoMensaje.MSJ_CONF_LU, new ListaUsuarios(datos)));
        } catch (Exception e) {
            ServerLogger.logError("Error al enviar la lista de usuarios al cliente '" + id + "'.");
        }
        controlador.release_read();
    }

    public String getUsuarioConFichero(String idFichero) {
        String nombreUser = "";
        controlador.request_read();
        for (Usuario user : datos.values()) {
            if (user.getFicheros().contains(idFichero)) {
                nombreUser = user.getId();
                break;
            }
        }
        controlador.release_read();
        return nombreUser;
    }

    public void guardarDatos() {
        //TODO
    }
}
