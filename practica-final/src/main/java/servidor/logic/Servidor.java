package servidor.logic;

import servidor.ui.ServerLogger;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Servidor {
    //public static final int MAX_CONCURRENT_USERS = 20;
    public static final int PORT_NUMBER = 2024;
    private static final String EXIT_WORD = "exit";

    private static ServerSocket ss;
    private volatile static boolean acceptingConnections = true;

    private List<OyenteCliente> clientes;
    private BaseDatos baseDatos;
    private TablaFlujos flujos;
    private TablaSolicitudes solicitudes;
    private int puertoNuevoCliente;

    public Servidor() throws IOException {
        ss = new ServerSocket(PORT_NUMBER);
        clientes = new ArrayList<OyenteCliente>();
        baseDatos = new BaseDatos();
        flujos = new TablaFlujos();
        solicitudes = new TablaSolicitudes();
        puertoNuevoCliente = 2025;
    }

    public void start() {
        //El hilo 'hc' se encarga de comprobar si el admin del servidor lo
        //cierra (a nuevas conexiones, las actuales se mantienen) en algún momento
        HiloCierre hCierre = new HiloCierre();
        hCierre.start();

        ServerLogger.log("Se ha iniciado el servidor y ha comenzado a aceptar clientes.");
        while (acceptingConnections) {
            try {
                Socket cs = ss.accept();
                OyenteCliente hc = new OyenteCliente(cs, baseDatos, flujos, solicitudes, puertoNuevoCliente);
                hc.start();
                clientes.add(hc);
                puertoNuevoCliente += 1;
            } catch (IOException e) {
                ServerLogger.logError("Error al aceptar un nuevo cliente.");
            }
        }
        shutdown(hCierre);
    }

    public void shutdown(HiloCierre hc) {
        //Esperamos a que se cierren las conexiones ya activas
        try {
            hc.join();
            for (OyenteCliente hCl : clientes) {
                hCl.join();
                ServerLogger.log("Se ha cerrado correctamente la conexión con el cliente " + hCl.getUserId());
            }
        } catch (InterruptedException e) {
            ServerLogger.logError("Error al cerrar un hilo.");
        }
        ServerLogger.log("Todas las conexiones se han cerrado correctamente y el servidor va a terminar su ejecucción.");
    }


    private static class HiloCierre extends Thread {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (acceptingConnections) {
                System.out.print("Para salir escriba 'exit': ");
                String input = scanner.nextLine();
                if (input.contains(EXIT_WORD)) {
                    ServerLogger.log("El servidor a recibido la señal de salida. No se aceptarán más clientes.");
                    acceptingConnections = false;

                    try {
                        ss.close();
                    } catch (IOException e) {
                        ServerLogger.logError("Error al cerrar el 'ServerSocket'.");
                    }
                    scanner.close();
                }
            }
        }
    }
}
