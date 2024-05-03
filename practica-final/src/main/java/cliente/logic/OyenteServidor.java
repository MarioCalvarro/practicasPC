package cliente.logic;

import cliente.ui.ClienteLogger;
import cliente.ui.ControlPrint;
import mensaje.Mensaje;
import mensaje.MsjString;
import mensaje.TipoMensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class OyenteServidor extends Thread {
    private static final int NUMERO_HILO = 1;

    private String nombre;
    private ServerSocket ss;
    private ObjectInputStream fIn;
    private int puerto;
    private ControlOutput controlOutput;
    private List<Thread> emisorReceptor;
    private int numeroHiloReceptor;
    private GestionFicheros ficheros;       //Lo necesita el receptor

    public OyenteServidor(String nombre, Socket cs, ControlOutput controlOutput, GestionFicheros ficheros) throws IOException, ClassNotFoundException {
        this.nombre = nombre;
        this.controlOutput = controlOutput;
        this.emisorReceptor = new ArrayList<>();
        this.ficheros = ficheros;

        Mensaje msj = null;
        this.numeroHiloReceptor = NUMERO_HILO + 1;

        fIn = new ObjectInputStream(cs.getInputStream());
        msj = (Mensaje) fIn.readObject();
        if (msj.getTipo() != TipoMensaje.MSJ_CONF_CONEXION) {
            ControlPrint.logError("No se ha recibido el mensaje 'MSJ_CONF_CONEXION'.");
            throw new RuntimeException();
        }
        this.puerto = Integer.parseInt(((MsjString) msj).getContenido());
        ControlPrint.log("Creando 'ServerSocket' en el puerto " + String.valueOf(puerto));
        ss = new ServerSocket(puerto);
    }

    @Override
    public void run() {
        while (!isInterrupted()) {
            gestionarPeticiones();
        }

        for (Thread t : emisorReceptor) {
            try {
                t.join();
            } catch (InterruptedException e) {
                ControlPrint.logError("Error al cerrar uno de los hilos de descarga/envio de archivos del cliente '" + this.nombre + "'.");
            }
        }
    }

    private void gestionarPeticiones() {
        Mensaje msj = null;
        try {
            msj = (Mensaje) fIn.readObject();
        } catch (ClassNotFoundException e) {
            ControlPrint.logError("Error al recibir un mensaje del servidor. Cerrando conexión.");
            interrupt();
            return;
        } catch (IOException e) {
            interrupt();
            return;
        }

        switch (msj.getTipo()) {
            case MSJ_CONF_CONEXION:
                ControlPrint.log("Conectado el cliente " + nombre);
                break;

            case MSJ_CONF_LU:
                String res = ((MsjString) msj).getContenido();
                ControlPrint.print("La información disponible en el sistema es:\n" + res);
                break;

            case MSJ_FICH_INEX:
                ControlPrint.logError("El fichero " + ((MsjString) msj).getContenido().toString() + " no está disponible.");
                break;

            case MSJ_PEDIR_FICHERO: // creamos nuevo hilo para controlar p2p

                //Esto bloqueará la llegada de nuevos mensajes hasta que el
                //receptor se conecte. Simplemente se irán almacenando
                String archivo = ((MsjString) msj).getContenido();
                String ip = "localhost";        //Si quisiesemos hacerlo más realista habría que pasar la ip de verdad
                try {
                    controlOutput.escribir(NUMERO_HILO, new MsjString(TipoMensaje.MSJ_PREPARADO_CS, archivo + " " + ip + " " + this.puerto));
                } catch (IOException e) {
                    ControlPrint.logError("Error al enviar un mensaje 'MSJ_PREPARADO_CS' para el archivo '" + archivo + "'. Cerrando conexión.");
                    interrupt();
                    return;
                }

                Socket sEmisor;
                try {
                    sEmisor = ss.accept();
                    //Start está en el constructor
                    HiloEmisor hiloEmisor = new HiloEmisor(nombre + "/" + archivo, sEmisor);
                    emisorReceptor.add(hiloEmisor);
                } catch (IOException e) {
                    ControlPrint.logError("Error al conectar con el receptor del fichero '" + archivo + "'. Cancelando.");
                }
                break;

            case MSJ_PREPARADO_SC: // nombre fichero un string con dos palabras
                String mensaje = ((MsjString) msj).getContenido();
                String[] separado = mensaje.split(" ");
                String archivo2 = separado[0];
                String ip2 = separado[1];
                String puerto = separado[2];
                //Start está en el constructor
                HiloReceptor hiloReceptor;
                try {
                    hiloReceptor = new HiloReceptor(nombre, archivo2, ip2, puerto, controlOutput, numeroHiloReceptor, ficheros);
                    emisorReceptor.add(hiloReceptor);
                    numeroHiloReceptor += 1;
                } catch (IOException e) {
                    ControlPrint.logError("Error al conectar con el emisor del fichero '" + archivo2 + "'. Cancelando.");
                }
                break;

            default:
                ControlPrint.logError("Error al recibir un mensaje del servidor. Cerrando conexión.");
                interrupt();
        }
    }
}
