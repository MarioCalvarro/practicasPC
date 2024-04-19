package cliente.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import mensaje.MsjListaUsuarios;
import mensaje.MsjString;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;


public class OyenteServidor extends Thread {
	
	private boolean conexionTerminada;
	private Socket cs;
	private String nombre;
	private ObjectOutputStream fOut;
	private ObjectInputStream fIn;
	
	public OyenteServidor(String nombre, Socket cs){
		this.conexionTerminada = false;
		this.cs = cs;
		this.nombre = nombre;	
		try {
            fOut = new ObjectOutputStream(cs.getOutputStream());
            fIn = new ObjectInputStream(cs.getInputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
    @Override
    public void run() {	
    	try {
			fOut.writeObject(new MsjString(TipoMensaje.MSJ_CONEXION, nombre));
            fOut.flush();
            MsjVacio msj = (MsjVacio) fIn.readObject();
            System.out.println("Conectado el cliente " + nombre);
            gestionarAcciones(pedirAcciones());
           
            
            
    	} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    private int pedirAcciones() { 	
        Scanner scanner = new Scanner(System.in);
    	System.out.println("Elige la acción que deseas hacer: ");
        System.out.println("1.Consultar información disponible en el sistema.");
        System.out.println("2.Descargar información deseada.");
        System.out.println("3.Terminar sesión.");
    	return Integer.parseInt(scanner.next());
    }
    private void gestionarAcciones(int num) {
    	switch(num) {
    	case 1:
    		consultarInformacion();
    		break;
    	case 2:
    		descargarInformacion();
    		break;
    	case 3:
    		finalizarConexion();
    		break;
    	default:
    		//TODO
    		throw new RuntimeErrorException(null, "Numero no valido");
    	}
    }
    
    private void consultarInformacion() throws IOException, ClassNotFoundException {
    	fOut.writeObject(new MsjVacio(TipoMensaje. MSJ_LU));
        fOut.flush();
        MsjListaUsuarios msj = (MsjListaUsuarios) fIn.readObject();
        msj.getContenido().toString();
    }
    private void descargarInformacion() {
    	
    }
    private void finalizarConexion() {
    	
    }
    
    
    public void main(String args[]) {
    	
    	while(!conexionTerminada) {
    		this.run();
    	}    	
    }
}
