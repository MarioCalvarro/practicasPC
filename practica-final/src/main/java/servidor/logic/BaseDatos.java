package servidor.logic;

import concurrencia.ControlAcceso;
import concurrencia.SemaforoRW;
import mensaje.MsjListaUsuarios;
import mensaje.TipoMensaje;
import servidor.ui.ServerLogger;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

public class BaseDatos {
    private static String RUTA_FICHERO_GUARDADO = "./servidor/basedatos.txt";

    private Map<String, Usuario> datos;
    private ControlAcceso controlador;

    public BaseDatos() {
        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(RUTA_FICHERO_GUARDADO);
        }
        catch (FileNotFoundException e) {
            ServerLogger.log("No se ha encontrado ninguna base de datos anterior. Creando una nueva.");
            datos = new HashMap<>();
        }
        ObjectInputStream in;
        try {
            in = new ObjectInputStream(fileIn);
            this.datos = (Map<String, Usuario>) in.readObject();
        } catch (Exception e) {
            ServerLogger.logError("Error al abrir el fichero de entrada de la base de datos.");
        }
        controlador = new SemaforoRW();
        try {
            fileIn.close();
        } catch (IOException e) {
            ServerLogger.logError("Error al cerrar el fichero de la base de datos.");
        }
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
        String nombreUser = null;
        controlador.request_read();
        for (Usuario user : datos.values()) {
            //Si tiene el fichero y está conectado
            if (user.getFicheros().contains(idFichero) && user.getConectado()) {
                nombreUser = user.getId();
                break;
            }
        }
        controlador.release_read();
        return nombreUser;
    }

    public void guardarDatos() {
        FileOutputStream fileOut = null;
        try {
            fileOut = new FileOutputStream(RUTA_FICHERO_GUARDADO);
        }
        catch (FileNotFoundException e) {
            //TODO
        }
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(fileOut);
            out.writeObject(datos);
        } catch (Exception e) {
            ServerLogger.logError("Error al abrir el fichero de entrada de la base de datos.");
        }
        try {
            fileOut.close();
        } catch (IOException e) {
            ServerLogger.logError("Error al cerrar el fichero de la base de datos.");
        }
    }
}
