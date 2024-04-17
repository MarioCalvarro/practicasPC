package pack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Servidor {
    private ServerSocket ss;
    private List<HiloSocket> clientes;

    public static void main(String[] args) {
        Servidor s = null;
        try {
            s = new Servidor();
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        s.start();
        s.shutdown();
    }

    public Servidor() throws IOException {
        ss = new ServerSocket(2024);
        clientes = new ArrayList<HiloSocket>();
    }

    public void start() {
        int num = 0;
        while (num < 5) {
            try {
                Socket cs = ss.accept();
                HiloSocket hc = new HiloSocket(cs);
                hc.start();
                clientes.add(hc);
                num += 1;
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void shutdown() {
        for (HiloSocket hc : clientes) {
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

    private static class HiloSocket extends Thread {
        private Socket cs;
        private ObjectInputStream fIn;
        private ObjectOutputStream fOut;

        public HiloSocket(Socket cs) {
            this.cs = cs;
            try {
                fIn = new ObjectInputStream(cs.getInputStream());
                fOut = new ObjectOutputStream(cs.getOutputStream());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            try {
                //Mensaje de conexión
                Mensaje msj = (Mensaje) fIn.readObject();
                if (msj.getTipo() != 0) {
                    throw new Exception("El cliente no ha enviado un mensaje de conexión.");
                }
                System.out.println("El servidor ha recibido: " + msj.getContenido());

                //Confirmación de conexión
                fOut.writeObject(new Mensaje(1, "Bienvenido cliente. El servidor ha recibido: " + msj.getContenido()));
                fOut.flush();

                //Cierre de conexión
                Mensaje msj2 = (Mensaje) fIn.readObject();
                if (msj2.getTipo() != 2) {
                    throw new Exception("El cliente no ha enviado un mensaje de cierre de conexión.");
                }
                System.out.println("El servidor ha recibido: " + msj2.getContenido());

                cs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
