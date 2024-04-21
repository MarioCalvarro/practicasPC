package cliente.ui;

import cliente.logic.Cliente;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenido cliente, introduzca su nombre");
        String nombre = sc.nextLine();
        Cliente c = new Cliente(nombre);
        while (true) {
            System.out.println("Elija la acción que desea realizar: ");
            break;
        }

        sc.close();
    }
}
