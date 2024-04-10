package pack;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Servidor {
    private ServerSocket ss;
    private List<HiloCliente> clientes;

    public Servidor() throws IOException {
        ss = new ServerSocket(1234);
        clientes = new ArrayList<HiloCliente>();
    }

    public void start() {
        while (!ss.isClosed()) {
            try {
                Socket cs = ss.accept();
                HiloCliente hc = new HiloCliente(cs);
                hc.start();
                clientes.add(hc);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        for (HiloCliente hc : clientes) {
            try {
                hc.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class HiloCliente extends Thread {
        private Socket cs;

        public HiloCliente(Socket cs) {
            this.cs = cs;
        }

        @Override
        public void run() {
            try(Scanner sc = new Scanner(cs.getInputStream());
                PrintWriter wr = new PrintWriter(cs.getOutputStream(), true)) 
            {
                while (sc.hasNextLine()) {
                    String rq = sc.nextLine();
                    System.out.println("El servidor ha recibido del cliente " + cs.getInetAddress().getHostName() + " el mensaje " + rq);
                    wr.println("Mensaje del servidor");
                }

                cs.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
