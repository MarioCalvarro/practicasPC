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
        ControlPrint.print("Bienvenido. Introduzca el nombre de usuario: ");
        String nombre = sc.next();
        sc.nextLine();
        try {
            c = new Cliente(nombre);
        } catch (ClassNotFoundException | IOException e) {
            ControlPrint.logError("Error al crear el cliente. Abortando.");
            sc.close();
            return;
        }
        while (!conexionTerminada) {
            int accion = pedirAcciones();
            gestionarAcciones(accion);
        }
        sc.close();
    }

    private static int pedirAcciones() {
        ControlPrint.print("Elija la acción que desea hacer: ");
        ControlPrint.print("1. Consultar información disponible en el sistema.");
        ControlPrint.print("2. Descargar información deseada.");
        ControlPrint.print("3. Terminar sesión.");
        int res;
        try {
            res = sc.nextInt();
        } catch (Exception e) {
            sc.nextLine();
            return -1;
        }
        sc.nextLine();
        return res;
    }

    private static void gestionarAcciones(int num) {
        switch (num) {
            case 1:
                try {
                    c.consultarInformacion();
                } catch (IOException e) {
                    ControlPrint.logError("Error al consultar la lista de usuarios.");
                }
                break;
            case 2:
                System.out.println("Introduzca el nombre del fichero que desea descargar: ");
                String fichero = sc.nextLine();
                try {
                    c.descargarInformacion(fichero);
                } catch (IOException e) {
                    ControlPrint.logError("Error al descargar un fichero.");
                }
                break;
            case 3:
                try {
                    c.finalizarConexion();
                } catch (IOException | InterruptedException e) {
                    ControlPrint.logError("Error al finalizar la conexión con el servidor.");
                }
                conexionTerminada = true;
                break;
            default:
                ControlPrint.logError("La acción introducida no es válida.");
        }
    }
}
