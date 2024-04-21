package servidor.ui;

import servidor.logic.Servidor;

import java.io.IOException;

public class App {
    public static void main(String[] args) {
        ServerLogger.log("El servidor va a iniciarse.");
        Servidor s = null;
        try {
            s = new Servidor();
        } catch (IOException e) {
            ServerLogger.logError("Error al crear un nuevo servidor. Abortando.");
            System.exit(1);
        }
        s.start();
    }
}
