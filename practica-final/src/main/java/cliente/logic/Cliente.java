package cliente.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;


public class Cliente {
    private String nombre;
    private ServerSocket ss;

    public Cliente(String nombre) {
        this.nombre = nombre;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void main(String args[]) throws UnknownHostException, IOException{ 	
    	Scanner scanner = new Scanner(System.in);
    	System.out.println("Bienvenido. Introduzca el nombre de usuario:");
    	String name = scanner.next();
    	Cliente cliente = new Cliente(name);
    	cliente.Start();	
    }
    
    private void Start() throws UnknownHostException, IOException {
    	ss = new ServerSocket(2024);
    	//TODO: Cambiar puerto
    	 Socket cs = new Socket("localhost", 2024);
         OyenteServidor hc = new OyenteServidor(nombre, cs);
         hc.start();
         try {
             hc.join();
         } catch (InterruptedException e) {
             e.printStackTrace();
         }	
    	
    }
    // pedir nombre ususario
    // relizar la conexion con el servidor 
    
    //se crea el oyente servidor para manejar la conexion
    //bucle (true mientras no se desconecte) para pedir acciones al cliente
}
