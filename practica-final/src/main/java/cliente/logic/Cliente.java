package cliente.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.management.RuntimeErrorException;

import mensaje.MsjListaUsuarios;
import mensaje.MsjString;
import mensaje.MsjUsuario;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;
import servidor.logic.Usuario;


public class Cliente {
    private String nombre;
	private ObjectOutputStream fOut;
	private boolean conexionTerminada;
	private Usuario usuario;
	private List<String> lista;



    public Cliente(String nombre) {
        this.nombre = nombre;
        this.conexionTerminada = false;
        this.lista = new ArrayList<String>();
        this.usuario= new Usuario (nombre, lista);
    }

    public String getNombre() {
        return nombre;
    }
    
    public void main(String args[]) throws UnknownHostException, IOException, ClassNotFoundException{ 	
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Bienvenido. Introduzca el nombre de usuario:");
    	String name = scanner.next();
    	Cliente cliente = new Cliente(name);
    	cliente.Start();	
    }
    
    private void Start() throws UnknownHostException, IOException, ClassNotFoundException {
    	//TODO: Cambiar puerto
    	Socket cs = new Socket("localhost", 2024);
        fOut = new ObjectOutputStream(cs.getOutputStream());
        OyenteServidor hc = new OyenteServidor(nombre, cs);
        hc.start();
        fOut.writeObject(new MsjUsuario(TipoMensaje.MSJ_CONEXION, usuario));
        fOut.flush();
        
        while(!conexionTerminada) {
            gestionarAcciones(pedirAcciones());
        }
        System.out.println("Has finalizado la conexión con el servidor.");
        try {
            hc.join();
        } catch (InterruptedException e) {
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
    
    private void gestionarAcciones(int num) throws ClassNotFoundException, IOException {
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
    }
    private void descargarInformacion() throws IOException {
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Introduce el nombre del fichero que deseas descargar: ");
    	String nombre = scanner.next();
    	fOut.writeObject(new MsjString(TipoMensaje. MSJ_PEDIR_FICHERO, nombre));
        fOut.flush();
    	
    }
    private void finalizarConexion() throws IOException {
    	fOut.writeObject(new MsjVacio(TipoMensaje. MSJ_CERRAR_CONEXION));
        fOut.flush();
        conexionTerminada = true;
    }
    // pedir nombre ususario
    // relizar la conexion con el servidor 
    
    //se crea el oyente servidor para manejar la conexion
    //bucle (true mientras no se desconecte) para pedir acciones al cliente
}
