package cliente.ui;

import cliente.logic.Cliente;

import java.io.IOException;
import java.util.Scanner;


public class App {
    private static Scanner sc;
    private static Cliente c;
    private static boolean conexionTerminada = false;

    public static void main(String[] args) {
        sc = new Scanner(System.in);
        System.out.println("Bienvenido. Introduzca el nombre de usuario: ");
        String nombre = sc.next(); sc.nextLine();
        try {
            c = new Cliente(nombre);
            while (!conexionTerminada) {
                int accion = pedirAcciones();              
    			gestionarAcciones(accion);		    
            }
            sc.close();
            
        } catch(IOException e) {
            ClienteLogger.logError("No se ha podio escribir correctamente.");
        } catch(InterruptedException e) {
            ClienteLogger.logError("Se ha interrumpido la ejecución.");
        } catch(ClassNotFoundException e) {
	        ClienteLogger.logError("El objeto recibido por el socket no es correcto.");
        }
    }

    private static int pedirAcciones() {
        System.out.println("Elija la acción que desea hacer: ");
        System.out.println("1. Consultar información disponible en el sistema.");
        System.out.println("2. Descargar información deseada.");
        System.out.println("3. Terminar sesión.");
        int res = sc.nextInt(); sc.nextLine();
        return res;
    }
    
    private static void gestionarAcciones(int num) throws IOException, InterruptedException  {
        switch (num) {
            case 1:
                c.consultarInformacion();
                break;
            case 2:
                System.out.println("Introduzca el nombre del fichero que desea descargar: ");
                String fichero = sc.nextLine();
                c.descargarInformacion(fichero);
                break;
            case 3:
                c.finalizarConexion();
                conexionTerminada = true;
                break;
            default:
                System.err.println("El número '" + num + "' no es válido.");
        }
    }
}
