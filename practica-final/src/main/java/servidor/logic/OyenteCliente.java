package servidor.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import mensaje.*;

class OyenteCliente extends Thread {
    private BaseDatos baseDatos;
    private TablaFlujos flujos;
    private ObjectInputStream fIn;      //El flujo de entrada no estará
                                        //compartido
    private ObjectOutputStream fOut;
    private String id;
    private int numThread;

    public OyenteCliente(Socket s, int numThread, BaseDatos baseDatos, TablaFlujos flujos)
    {
        this.numThread = numThread;
        this.baseDatos = baseDatos;
        this.flujos = flujos;
        try {
            fIn = new ObjectInputStream(s.getInputStream());
            fOut = new ObjectOutputStream(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            establecerConexion();
            iniciarEscucha();
        } catch (Exception e) {
            return;
        }
    }

    private void establecerConexion() {
        MsjUsuario msj;
        try {
            msj = (MsjUsuario) fIn.readObject();
            if (msj.getTipo() != TipoMensaje.MSJ_CONEXION) {
                //TODO: Error
                throw new Exception();
            }
            Usuario user = msj.getContenido();
            this.id = user.getId();
            //TODO: Logger
            System.out.println("Solicitud de conexión de: " + this.id);
            System.out.println("Enviando confirmación de conexión");
            //No hace falta control de concurrencia aquí porque todavía no está
            //compartido
            fOut.writeObject(new MsjVacio(TipoMensaje.MSJ_CONF_CONEXION));

            //Actualizamos las tablas
            this.flujos.nuevoHilo(id, fOut);
            this.baseDatos.conexionUsuario(user);
        } catch (Exception e) {
            //TODO: Mejorar salida
            interrupt();
        }
    }

    private void iniciarEscucha() throws ClassNotFoundException, IOException {
        Mensaje msj;
        boolean conectados = true;
        while (conectados) {
            msj = (Mensaje) fIn.readObject();
            switch(msj.getTipo()) {
                case MSJ_LU:
                    //Enviar la lista de usuarios
                    baseDatos.enviarUsuarios(id, flujos);
                    break;
                case MSJ_PEDIR_FICHERO:
                    String nombreFichero = ((MsjString) msj).getContenido();
                    solicitarFichero(nombreFichero);
                    break;
                case MSJ_PREPARADO_SC:
                    String ficheroIpPort = ((MsjString) msj).getContenido();

                    String[] palabras = ficheroIpPort.split(" ");
                    break;
                case MSJ_FIN_EMISION_FICHERO:
                    actualizarBaseDatos();
                    break;
                case MSJ_CERRAR_CONEXION:
                    //TODO
                    break;
                default:
                    //TODO: Log
                    interrupt();
                    break;
            }
        }
    }

    private void solicitarFichero(String nombreFichero) throws IOException {
        String nombreEmisor = baseDatos.getUsuarioConFichero(nombreFichero);
        if (nombreEmisor == null) {
            //No se ha podido encontrar el fichero en la base de datos
            flujos.escribir(id, new MsjVacio(TipoMensaje.MSJ_FICH_INEX));
        }
        flujos.escribir(nombreEmisor, new MsjString(TipoMensaje.MSJ_PEDIR_FICHERO, nombreFichero));
    }

    private void actualizarBaseDatos() {

    }

}
