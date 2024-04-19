package servidor.logic;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;

class OyenteCliente extends Thread {
    private Map<String, Usuario> baseDatos;
    private Map<String, ObjectInputStream> tablaFlujosIn;
    private Map<String, ObjectOutputStream> tablaFlujosOut;

    public OyenteCliente(Socket s, Map<String, Usuario> baseDatos, 
            Map<String, ObjectInputStream> tablaFlujosIn, 
            Map<String, ObjectOutputStream> tablaFlujosOut)
    {
        this.baseDatos = baseDatos;
        this.tablaFlujosIn = tablaFlujosIn;
        this.tablaFlujosOut = tablaFlujosOut;   
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        super.run();
    }
}
