package cliente.logic;

import concurrencia.Lock;
import mensaje.Mensaje;
import mensaje.MsjListaUsuarios;
import mensaje.MsjString;
import mensaje.TipoMensaje;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;


public class OyenteServidor extends Thread {
    public static final int PORT_NUMBER = 2025;

    private boolean conexionTerminada;
    private String nombre;
    private ServerSocket ss;
    private ObjectInputStream fIn;
    private ObjectOutputStream fOut;
    private Lock lock;
    private String puerto;
    private ControlOutput controlOutput;

    public OyenteServidor(String nombre, Socket cs) {
        this.conexionTerminada = false;
        this.nombre = nombre;
        try {
            fIn = new ObjectInputStream(cs.getInputStream());
            fOut = new ObjectOutputStream(cs.getOutputStream());
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            ss = new ServerSocket(PORT_NUMBER);

            while (!conexionTerminada) {
                gestionarPeticiones();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void gestionarPeticiones() throws ClassNotFoundException, IOException {
        Mensaje msj = (Mensaje) fIn.readObject();
        switch (msj.getTipo()) {
            case MSJ_CONF_CONEXION:
                System.out.println("Conectado el cliente " + nombre);
                break;

            case MSJ_CONF_LU:
                ((MsjListaUsuarios) msj).getContenido().toString();
                break;

            case MSJ_FICH_INEX:
                System.out.println("El fichero " + ((MsjString) msj).getContenido().toString() + " no está disponible.");
                break;

            case MSJ_PEDIR_FICHERO: // creamos nuevo hilo para controlar p2p
                HiloEmisor hiloEmisor = new HiloEmisor(((MsjString) msj).getContenido().toString(), ss);
                lock.takeLock(MAX_PRIORITY);
                fOut.writeObject(new MsjString(TipoMensaje.MSJ_PREPARADO_CS, ((MsjString) msj).getContenido() + " " + this.nombre + " " + this.puerto));
                fOut.flush();
                lock.releaseLock(MAX_PRIORITY);
                try {
                    hiloEmisor.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                break;

            //TODO: Usando split
            case MSJ_PREPARADO_SC: // nombre fichero un string con dos palabras
                Scanner scanner = new Scanner(((MsjString) msj).getContenido().toString());
                String archivo = scanner.next(); // Lee la siguiente palabra
                String id = scanner.next(); // Lee la siguiente palabra
                String puerto = scanner.next(); // Lee la siguiente palabra
                scanner.close();
                HiloReceptor hiloReceptor = new HiloReceptor(archivo, id, puerto);
                try {
                    hiloReceptor.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                lock.takeLock(MAX_PRIORITY);
                fOut.writeObject(new MsjString(TipoMensaje.MSJ_FIN_EMISION_FICHERO, archivo));
                fOut.flush();
                lock.releaseLock(MAX_PRIORITY);
                break;

            default:
        }
    }
}
