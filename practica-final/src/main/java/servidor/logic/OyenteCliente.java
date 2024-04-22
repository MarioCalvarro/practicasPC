package servidor.logic;

import mensaje.*;
import servidor.ui.ServerLogger;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.ExecutionException;

class OyenteCliente extends Thread {
    private volatile boolean conexionCorrecta;
    private BaseDatos baseDatos;
    private TablaFlujos flujos;
    private TablaSolicitudes solicitudes;
    private ObjectInputStream fIn;      //El flujo de entrada no estará
    //compartido
    private ObjectOutputStream fOut;
    private String id;
    private int puertoNuevoCliente;

    public OyenteCliente(Socket s, BaseDatos baseDatos, TablaFlujos flujos, TablaSolicitudes solicitudes, int puertoNuevoCliente) {
        this.conexionCorrecta = true;
        this.baseDatos = baseDatos;
        this.flujos = flujos;
        this.solicitudes = solicitudes;
        this.puertoNuevoCliente = puertoNuevoCliente;
        try {
            fIn = new ObjectInputStream(s.getInputStream());
            fOut = new ObjectOutputStream(s.getOutputStream());
        } catch (Exception e) {
            ServerLogger.logError("Error al tomar los flujos del canal con un cliente. Abortando conexión.");
            interrupt();
        }
    }

    public String getUserId() {
        return this.id;
    }

    @Override
    public void run() {
        ServerLogger.log("El servidor va a iniciar la conexión con un nuevo cliente.");
        establecerConexion();
        if (conexionCorrecta) {
            ServerLogger.log("Iniciando escucha activa en el canal con el cliente '" + this.id + "'.");
            iniciarEscucha();
        }
    }

    private void establecerConexion() {
        MsjUsuario msj = null;
        try {
            msj = (MsjUsuario) fIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            ServerLogger.logError("Error al leer el mensaje de conexión de un cliente.");
        }

        if (msj.getTipo() != TipoMensaje.MSJ_CONEXION) {
            ServerLogger.logError("Error al intentar conectar con un cliente. No se ha recibido el mensaje de conexión.");
            conexionCorrecta = false;
            return; 
        }

        Usuario user = msj.getContenido();
        this.id = user.getId();
        ServerLogger.log("Solicitud de conexión de '" + this.id + "'.");

        ServerLogger.log("Enviando confirmación de conexión a '" + this.id + "'.");
        try {
            //No hace falta control de concurrencia aquí porque todavía no está
            //compartido
            fOut.writeObject(new MsjString(TipoMensaje.MSJ_CONF_CONEXION, String.valueOf(puertoNuevoCliente)));
        } catch (IOException e) {
            ServerLogger.logError("Error al enviar el mensaje de confirmación de conexión.");
        }

        //Actualizamos las tablas
        this.flujos.nuevoHilo(id, fOut);
        this.baseDatos.conexionUsuario(user);
        ServerLogger.log("La conexión ha sido establecida con el cliente '" + this.id + "'.");
    }

    private void iniciarEscucha() {
        Mensaje msj;
        while (conexionCorrecta) {
            try {
                msj = (Mensaje) fIn.readObject();
            } catch (ClassNotFoundException | IOException e) {
                ServerLogger.logError("Error al recibir un mensaje del cliente. Cerrando conexión.");
                conexionCorrecta = false;
                return;
            }

            switch (msj.getTipo()) {
                case MSJ_LU:
                    ServerLogger.log("Recibida solicitud de envío de lista de usuarios de '" + this.id + "'.");
                    baseDatos.enviarUsuarios(id, flujos);
                    ServerLogger.log("Enviada la lista de usuario a '" + this.id + "'.");
                    break;
                case MSJ_PEDIR_FICHERO:
                    String nombreFichero = ((MsjString) msj).getContenido();
                    ServerLogger.log("Recibida solicitud de envío del fichero '" + nombreFichero + "' del cliente '" + this.id + "'.");
                    solicitarFichero(nombreFichero);
                    break;
                case MSJ_PREPARADO_CS:
                    String ficheroIpPort = ((MsjString) msj).getContenido();
                    enviarPreparadoSC(ficheroIpPort);
                    break;
                case MSJ_FIN_EMISION_FICHERO:
                    String fichero = ((MsjString) msj).getContenido();
                    ServerLogger.log("El cliente '" + this.id + "' acaba de completar la descarga del fichero '" + fichero + "'. Actualizando base de datos.");
                    baseDatos.nuevoFicheroEnUser(this.id, fichero);
                    break;
                case MSJ_CERRAR_CONEXION:
                    ServerLogger.log("El cliente '" + this.id + "' acaba de desconectarse. Actualizando base de datos.");
                    baseDatos.desconexionUsuario(this.id);
                    conexionCorrecta = false;
                    break;
                default:
                    ServerLogger.logError("El cliente '" + this.id + "' acaba de enviar un mensaje erróneo.");
                    break;
            }
        }
    }

    private void solicitarFichero(String nombreFichero) {
        String nombreEmisor = baseDatos.getUsuarioConFichero(nombreFichero);
        if (nombreEmisor == null) {
            ServerLogger.logError("El fichero '" + nombreFichero + "' no se encuentra en la base de datos. Enviando 'Fichero inexistente' al cliente '" + this.id + "'.");
            flujos.escribir(id, new MsjString(TipoMensaje.MSJ_FICH_INEX, nombreFichero));
            return;
        }

        ServerLogger.log("El fichero '" + nombreFichero + "' se encuentra en el cliente '" + nombreEmisor + "'. Enviando solicitud de fichero.");
        solicitudes.nuevaSolicitud(nombreFichero, id);
        flujos.escribir(nombreEmisor, new MsjString(TipoMensaje.MSJ_PEDIR_FICHERO, nombreFichero));
    }

    private void enviarPreparadoSC(String ficheroIpPort) {
        //Separado: Nombre fichero preparado - IP emisor - Puerto emisor
        String[] separado = ficheroIpPort.split(" ");
        ServerLogger.log("El cliente '" + this.id + "' tiene preparado el fichero '" + separado[0] + "'.");

        String nombreReceptor = solicitudes.getSiguienteReceptor(separado[0]);

        ServerLogger.log("Enviando mensaje de preparado fichero '" + separado[0] + "' al receptor '" + nombreReceptor + "'.");
        flujos.escribir(nombreReceptor, new MsjString(TipoMensaje.MSJ_PREPARADO_SC, ficheroIpPort));
    }
}
