package servidor.logic;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Servidor {
    public static final int MAX_CONCURRENT_USERS = 20;
    public static final int PORT_NUMBER = 2024;
    private static final String EXIT_WORD = "exit";
    private volatile static boolean acceptingConnections = true;

    private ServerSocket ss;
    private List<OyenteCliente> clientes;
    private BaseDatos baseDatos;
    private TablaFlujos flujos;
    private TablaSolicitudes solicitudes;

    public static void main(String[] args) {
        Servidor s = null;
        try {
            s = new Servidor();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        //El hilo 'hc' se encarga de comprobar si el admin del servidor lo
        //cierra (a nuevas conexiones, las actuales se mantienen) en algún momento
        HiloCierre hc = new HiloCierre(); hc.start();
        s.start();
        //Esperamos a que se cierren las conexiones ya activas
        s.shutdown(hc);
    }

    public Servidor() throws IOException {
        ss = new ServerSocket(PORT_NUMBER);
        clientes = new ArrayList<OyenteCliente>();
        baseDatos = new BaseDatos();
        flujos = new TablaFlujos();
    }

    public void start() {
        while (acceptingConnections) {
            try {
                Socket cs = ss.accept();
                OyenteCliente hc = new OyenteCliente(cs, baseDatos, flujos, solicitudes);
                hc.start();
                clientes.add(hc);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown(HiloCierre hc) {
        try {
            hc.join();
            for (OyenteCliente hCl : clientes) {
                hCl.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static class HiloCierre extends Thread {
        @Override
        public void run() {
            Scanner scanner = new Scanner(System.in);
            while (acceptingConnections) {
                System.out.print("To exit type 'exit': ");
                String input = scanner.nextLine();
                if (input.contains(EXIT_WORD)) {
                    //TODO: Esto no funciona correctamente
                    acceptingConnections = false;
                    scanner.close();
                }
            }
        }
    }
}
