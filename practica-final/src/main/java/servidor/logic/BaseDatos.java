package servidor.logic;

import concurrencia.ControlAcceso;
import concurrencia.SemaforoRW;
import mensaje.MsjString;
import mensaje.TipoMensaje;
import servidor.ui.ServerLogger;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class BaseDatos {
    private static String RUTA_FICHERO_GUARDADO = "./servidor/basedatos.txt";
    private static String RUTA_CARPETA_FICHERO_GUARDADO = "./servidor/";

    private Map<String, Usuario> datos;
    private ControlAcceso controlador;

    public BaseDatos() {
        controlador = new SemaforoRW();

        FileInputStream fileIn = null;
        try {
            fileIn = new FileInputStream(RUTA_FICHERO_GUARDADO);
        } catch (FileNotFoundException e) {
            ServerLogger.log("No se ha encontrado ninguna base de datos anterior. Creando una nueva.");
            datos = new HashMap<>();
            return;
        }

        ObjectInputStream in;
        try {
            in = new ObjectInputStream(fileIn);
            this.datos = (Map<String, Usuario>) in.readObject();
        } catch (Exception e) {
            ServerLogger.logError("Error al abrir el fichero de entrada de la base de datos.");
        }

        try {
            fileIn.close();
        } catch (IOException e) {
            ServerLogger.logError("Error al cerrar el fichero de la base de datos.");
        }
    }

    public void conexionUsuario(Usuario user) throws InterruptedException {
        controlador.request_write();
        String id = user.getId();
        datos.put(id, user);
        datos.get(id).conectar();
        controlador.release_write();
    }

    public void desconexionUsuario(String id) throws InterruptedException {
        controlador.request_write();
        datos.get(id).desconectar();
        controlador.release_write();
    }

    public void nuevoFicheroEnUser(String idUser, String fichero) throws InterruptedException {
        controlador.request_write();
        datos.get(idUser).nuevoFichero(fichero);
        controlador.release_write();
    }

    public void eliminarUsuario(String id, Usuario user) throws InterruptedException {
        controlador.request_write();
        datos.remove(id);
        controlador.release_write();
    }

    public void enviarUsuarios(String id, TablaFlujos flujos) throws InterruptedException {
        controlador.request_read();
        try {
            ListaUsuarios lista = new ListaUsuarios(datos);
            flujos.escribir(id, new MsjString(TipoMensaje.MSJ_CONF_LU, lista.toString()));
        } catch (Exception e) {
            ServerLogger.logError("Error al enviar la lista de usuarios al cliente '" + id + "'.");
        }
        controlador.release_read();
    }

    public String getUsuarioConFichero(String idFichero) throws InterruptedException {
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
        } catch (FileNotFoundException e) {
            File file = new File(RUTA_FICHERO_GUARDADO);
            if (!file.exists()) {
                try {
                    File carpeta = new File(RUTA_CARPETA_FICHERO_GUARDADO);
                    carpeta.mkdirs();
                    file.createNewFile();
                } catch (IOException e1) {
                    ServerLogger.logError("Error al crear el fichero de base de datos. Cancelando.");
                    return;
                }
            }
            try {
                fileOut = new FileOutputStream(RUTA_FICHERO_GUARDADO);
            } catch (FileNotFoundException e1) {
                ServerLogger.logError("Error al crear el fichero de base de datos.");
            }
        }
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(fileOut);
            controlador.request_read();
            out.writeObject(datos);
            controlador.release_read();
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
