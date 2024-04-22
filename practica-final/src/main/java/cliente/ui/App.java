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
        } catch (ClassNotFoundException | IOException e) {
            ClienteLogger.logError("Error al crear el cliente. Abortando.");
        }
        while (!conexionTerminada) {
            int accion = pedirAcciones();              
            gestionarAcciones(accion);		    
        }
        sc.close();
    }

    private static int pedirAcciones() {
        System.out.println("Elija la acción que desea hacer: ");
        System.out.println("1. Consultar información disponible en el sistema.");
        System.out.println("2. Descargar información deseada.");
        System.out.println("3. Terminar sesión.");
        int res = sc.nextInt(); sc.nextLine();
        return res;
    }
    
    private static void gestionarAcciones(int num)  {
        switch (num) {
            case 1:
                try {
                    c.consultarInformacion();
                } catch (IOException e) {
                    ClienteLogger.logError("Error al consultar la lista de usuarios.");
                }
                break;
            case 2:
                System.out.println("Introduzca el nombre del fichero que desea descargar: ");
                String fichero = sc.nextLine();
                try {
                    c.descargarInformacion(fichero);
                } catch (IOException e) {
                    ClienteLogger.logError("Error al descargar un fichero.");
                }
                break;
            case 3:
                try {
                    c.finalizarConexion();
                } catch (IOException | InterruptedException e) {
                    ClienteLogger.logError("Error al finalizar la conexión con el servidor.");
                }
                conexionTerminada = true;
                break;
            default:
                System.err.println("El número '" + num + "' no es válido.");
        }
    }
}
