package cliente.ui;

import cliente.logic.Cliente;

import java.util.Scanner;

import javax.management.RuntimeErrorException;

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
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        while (!conexionTerminada) {
            int accion = pedirAcciones();
            try {
                gestionarAcciones(accion);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        sc.close();
    }

    private static int pedirAcciones() {
        System.out.println("Eliga la acción que desea hacer: ");
        System.out.println("1. Consultar información disponible en el sistema.");
        System.out.println("2. Descargar información deseada.");
        System.out.println("3. Terminar sesión.");
        int res = sc.nextInt(); sc.nextLine();
        return res;
    }
    
    private static void gestionarAcciones(int num) throws Exception {
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
                //TODO
                throw new RuntimeErrorException(null, "Número no válido");
        }
    }
}
