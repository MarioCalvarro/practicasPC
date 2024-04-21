package cliente.logic;

import mensaje.MsjVacio;
import mensaje.TipoMensaje;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class HiloReceptor extends Thread {
    private Socket cs;
    private ObjectInputStream fIn;
    private FileOutputStream fileOutputStream;
    private ObjectOutputStream fOut;


    public HiloReceptor(String archivo, String id, String puerto) throws NumberFormatException, UnknownHostException, IOException {
        cs = new Socket(id, Integer.parseInt(puerto));
        fileOutputStream = new FileOutputStream(archivo + ".txt");

        try {
            fIn = new ObjectInputStream(cs.getInputStream());
            fOut = new ObjectOutputStream(cs.getOutputStream());
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        // Crear buffer para escritura del archivo
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Leer datos del socket y escribirlos en el archivo
        try {
            while ((bytesRead = fIn.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
                fOut.writeObject(new MsjVacio(TipoMensaje.MSJ_CERRAR_CONEXION));
                fOut.flush();

                fIn.close();
                fileOutputStream.close();
                fOut.close();
                cs.close();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
