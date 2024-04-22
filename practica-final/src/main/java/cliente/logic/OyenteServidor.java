package cliente.logic;

import cliente.ui.ClienteLogger;
import mensaje.Mensaje;
import mensaje.MsjListaUsuarios;
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
    private int puertoCliente;
    private ControlOutput controlOutput;
    private List<Thread> emisorReceptor;

    public OyenteServidor(String nombre, Socket cs, ControlOutput controlOutput) throws IOException, ClassNotFoundException {
        this.nombre = nombre;
        this.controlOutput = controlOutput;
        this.emisorReceptor = new ArrayList<>();
        Mensaje msj = null;

        fIn = new ObjectInputStream(cs.getInputStream());
        msj = (Mensaje) fIn.readObject();
        if (msj.getTipo() != TipoMensaje.MSJ_CONF_CONEXION) {
            ClienteLogger.logError("No se ha recibido el mensaje 'MSJ_CONF_CONEXION'.");
            throw new RuntimeException();
        }
        puertoCliente = Integer.parseInt(((MsjString) msj).getContenido());
        ClienteLogger.log("Creando 'ServerSocket' en el puerto " + String.valueOf(puertoCliente));
        ss = new ServerSocket(puertoCliente);
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
                ClienteLogger.logError("Error al cerrar uno de los hilos de descarga/envio de archivos del cliente '" + this.nombre + "'.");
            }
        }
    }

    private void gestionarPeticiones() {
        Mensaje msj;
        try {
            msj = (Mensaje) fIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            ClienteLogger.logError("Error al recibir un mensaje del servidor. Cerrando conexión.");
            interrupt();
            return;
        }

        switch (msj.getTipo()) {
            case MSJ_CONF_CONEXION:
                ClienteLogger.log("Conectado el cliente " + nombre);
                break;

            case MSJ_CONF_LU:
                String res = ((MsjListaUsuarios) msj).getContenido().toString();
                System.out.println("La información disponible en el sistema es:\n" + res);
                break;

            case MSJ_FICH_INEX:
                ClienteLogger.logError("El fichero " + ((MsjString) msj).getContenido().toString() + " no está disponible.");
                break;

            case MSJ_PEDIR_FICHERO: // creamos nuevo hilo para controlar p2p

                //Esto bloqueará la llegada de nuevos mensajes hasta que el
                //receptor se conecte. Simplemente se irán almacenando
                String archivo = ((MsjString) msj).getContenido();
                String ip = "localhost";
                try {
                    controlOutput.escribir(NUMERO_HILO, new MsjString(TipoMensaje.MSJ_PREPARADO_CS, archivo + " " + ip + " " + this.puertoCliente));
                } catch (IOException e) {
                    ClienteLogger.logError("Error al enviar un mensaje 'MSJ_PREPARADO_CS' para el archivo '" + archivo + "'. Cerrando conexión.");
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
                    ClienteLogger.logError("Error al conectar con el receptor del fichero '" + archivo + "'. Cancelando.");
                }
                break;

            case MSJ_PREPARADO_SC: // nombre fichero un string con tres palabras
                String mensaje = ((MsjString) msj).getContenido();
                String[] separado = mensaje.split(" ");
                String archivo2 = separado[0];
                String ip2 = separado[1];
                String puerto = separado[2];
                //Start está en el constructor
                HiloReceptor hiloReceptor;
                try {
                    hiloReceptor = new HiloReceptor(nombre + "/" + archivo2, ip2, puerto, controlOutput);
                    emisorReceptor.add(hiloReceptor);
                } catch (IOException e) {
                    ClienteLogger.logError("Error al conectar con el receptor del fichero '" + archivo2 + "'. Cancelando.");
                }


                break;

            default:
                ClienteLogger.logError("Error al recibir un mensaje del servidor. Cerrando conexión.");
                interrupt();
        }
    }
}
