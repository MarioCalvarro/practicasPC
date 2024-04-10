package cliente.ui;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenido cliente, introduzca su nombre");    
        String nombre = sc.nextLine();
        sc.close();
    }
}
