package pack;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
    private String nombre;
    private List<HiloSocket> clientes;

    public static void main(String[] args) {
        Cliente c = null;
        try {
            c = new Cliente(args[0]);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
        c.start();
    }

    public Cliente(String nombre) throws IOException {
        this.nombre = nombre;
        clientes = new ArrayList<HiloSocket>();
    }

    public void start() {
        try {
            Socket cs = new Socket("localhost", 1234);
            HiloSocket hc = new HiloSocket(nombre, cs);
            hc.start();
            clientes.add(hc);
            for (HiloSocket hl : clientes) {
                try {
                    hl.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class HiloSocket extends Thread {
        private Socket cs;
        private String nombre;
        private ObjectInputStream fIn;
        private ObjectOutputStream fOut;

        public HiloSocket(String nombre, Socket cs) {
            this.nombre = nombre;
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
                fOut.writeObject(new Mensaje(0, "Hola servidor. El cliente " + nombre + " desea conectarse."));

                //Confirmación de conexión
                Mensaje msj = (Mensaje) fIn.readObject();
                if (msj.getTipo() != 1) {
                    throw new Exception("El servidor no ha enviado un mensaje de confirmación de conexión.");
                }
                System.out.println("El cliente ha recibido " + msj.getContenido());

                //Cierre de conexión
                fOut.writeObject(new Mensaje(2, "Adiós servidor. El cliente " + nombre + " se desconecta."));

                cs.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
