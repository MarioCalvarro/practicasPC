package cliente.logic;

import mensaje.Mensaje;
import mensaje.MsjListaUsuarios;
import mensaje.MsjString;
import mensaje.TipoMensaje;

import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import cliente.ui.ClienteLogger;


public class OyenteServidor extends Thread {
    private static final int NUMERO_HILO = 1;

    private String nombre;
    private ServerSocket ss;
    private ObjectInputStream fIn;
    private String puerto;
    private ControlOutput controlOutput;
    private List<Thread> emisorReceptor;

    public OyenteServidor(String nombre, Socket cs, ControlOutput controlOutput) {
        this.nombre = nombre;
        this.controlOutput = controlOutput;
        this.emisorReceptor = new ArrayList<>();
        Mensaje msj = null;
        try {
            fIn = new ObjectInputStream(cs.getInputStream());
            msj = (Mensaje) fIn.readObject();
            if (msj.getTipo() != TipoMensaje.MSJ_CONF_CONEXION) {
                //TODO
                throw new Exception();
            }
            int puertoCliente = Integer.parseInt(((MsjString) msj).getContenido());
            ClienteLogger.log("Creando 'ServerSocket' en el puerto " + String.valueOf(puertoCliente));
            ss = new ServerSocket(puertoCliente);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            while (!isInterrupted()) {
                gestionarPeticiones();
            }

            for (Thread t : emisorReceptor) {
                t.join();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void gestionarPeticiones() {
        try {
            Mensaje msj = (Mensaje) fIn.readObject();

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
                    controlOutput.escribir(NUMERO_HILO, new MsjString(TipoMensaje.MSJ_PREPARADO_CS, ((MsjString) msj).getContenido() + " " + this.nombre + " " + this.puerto));

                    Socket sEmisor = ss.accept();
                    //Start está en el constructor
                    HiloEmisor hiloEmisor = new HiloEmisor(((MsjString) msj).getContenido().toString(), sEmisor);
                    emisorReceptor.add(hiloEmisor);
                    break;

                case MSJ_PREPARADO_SC: // nombre fichero un string con dos palabras
                    String mensaje = ((MsjString) msj).getContenido();
                    String[] separado = mensaje.split(" ");
                    String archivo = separado[0]; String ip = separado[1]; String puerto = separado[2];
                    //Start está en el constructor
                    HiloReceptor hiloReceptor = new HiloReceptor(archivo, ip, puerto, controlOutput);
                    emisorReceptor.add(hiloReceptor);
                    break;

                default:
            }
        } catch (Exception e) {
            ClienteLogger.log("Fin de conexión con el servidor.");
            this.interrupt();
        }
    }
}
