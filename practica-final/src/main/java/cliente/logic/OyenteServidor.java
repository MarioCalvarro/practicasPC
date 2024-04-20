package cliente.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import mensaje.Mensaje;
import mensaje.MsjListaUsuarios;
import mensaje.MsjString;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;


public class OyenteServidor extends Thread {
	
	private boolean conexionTerminada;
	private Socket cs;
	private String nombre;
    private ServerSocket ss;
	private ObjectInputStream fIn;
	
	public OyenteServidor(String nombre, Socket cs){
		this.conexionTerminada = false;
		this.cs = cs;
		this.nombre = nombre;	
		try {
            fIn = new ObjectInputStream(cs.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    @Override
    public void run() {	
    		try {
				ss = new ServerSocket(2025);
	    		gestionarPeticiones();

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	
    }
   
    public void main(String args[]) {	
    	while(!conexionTerminada) {
    		this.run();
    	}    	
    }
    
    private void gestionarPeticiones() throws ClassNotFoundException, IOException {
    	Mensaje msj = (Mensaje) fIn.readObject();   	
    	switch(msj.getTipo()) {
    		case MSJ_CONF_CONEXION:
    			System.out.println("Conectado el cliente " + nombre);               	
    			break;
    		case MSJ_CONF_LU:
                ((MsjListaUsuarios)msj).getContenido().toString();
                break;
    		case MSJ_FICH_INEX:
    			System.out.println("El fichero " + ((MsjString)msj).getContenido().toString() + " no está disponible.");
    		case MSJ_PREPARADO_SC:
    			break;
    		case MSJ_INICIO_EMISION_FICHERO:
    			break;
    		case MSJ_FIN_EMISION_FICHERO:
    			break;
    		default:
    			
    	}
    }
}
