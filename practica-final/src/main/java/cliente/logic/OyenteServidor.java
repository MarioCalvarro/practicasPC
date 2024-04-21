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
    public static final int NUMERO_HILO = 1;


    private boolean conexionTerminada;
    private String nombre;
    private ServerSocket ss;
    private ObjectInputStream fIn;
    private String puerto;
    private ControlOutput controlOutput;

    public OyenteServidor(String nombre, Socket cs, ControlOutput controlOutput) {
        this.conexionTerminada = false;
        this.nombre = nombre;
        this.controlOutput = controlOutput;
        try {
            fIn = new ObjectInputStream(cs.getInputStream());
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
                ((MsjListaUsuarios) msj).getContenido().getUsuarios().toString();
                break;

            case MSJ_FICH_INEX:
                System.out.println("El fichero " + ((MsjString) msj).getContenido().toString() + " no está disponible.");
                break;

            case MSJ_PEDIR_FICHERO: // creamos nuevo hilo para controlar p2p
                HiloEmisor hiloEmisor = new HiloEmisor(((MsjString) msj).getContenido().toString(), ss);
                controlOutput.escribir(NUMERO_HILO, new MsjString(TipoMensaje.MSJ_PREPARADO_CS, ((MsjString) msj).getContenido() + " " + this.nombre + " " + this.puerto));
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
                String[] separado = scanner.nextLine().split(" ");
                String archivo = separado[0]; 
                String id = separado[1]; 
                String puerto = separado[2];
                scanner.close();
                HiloReceptor hiloReceptor = new HiloReceptor(archivo, id, puerto);
                try {
                    hiloReceptor.join();
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                controlOutput.escribir(NUMERO_HILO,new MsjString(TipoMensaje.MSJ_FIN_EMISION_FICHERO, archivo));
                break;

            default:
        }
    }
}
