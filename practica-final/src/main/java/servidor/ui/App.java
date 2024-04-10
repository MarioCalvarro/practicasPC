package servidor.ui;

import java.util.Scanner;

import servidor.logic.Servidor;

public class App {
	
	
	
	public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("Bienvenido al Servidor");    
        Servidor s = new Servidor();
        while(true) {
            System.out.println("Elije la acción que deseas hacer");
            break;
        }
        sc.close();
    }
}
