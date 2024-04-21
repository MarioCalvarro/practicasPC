package cliente.logic;

import mensaje.Mensaje;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import cliente.ui.ClienteLogger;

public class HiloEmisor extends Thread {
    private ServerSocket ss;
    private Socket cs;
    private FileInputStream fileInputStream;
    private ObjectOutputStream fOut;
    private ObjectInputStream fIn;
    private String archivo;

    public HiloEmisor(String archivo, ServerSocket ss) {
        this.ss = ss;
        this.run();
    }

    @Override
    public void run() {
        try {
            cs = ss.accept();

            fOut = new ObjectOutputStream(cs.getOutputStream());
            fIn = new ObjectInputStream(cs.getInputStream());

            fileInputStream = new FileInputStream(archivo);
            fOut.writeObject(new MsjVacio(TipoMensaje.MSJ_INICIO_EMISION_FICHERO));
            fOut.flush();

            // Crear buffer para lectura del archivo
            byte[] buffer = new byte[1024];
            int bytesRead;


            // Leer el archivo y enviarlo por el socket
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                fOut.write(buffer, 0, bytesRead);
            }

            Mensaje msj = (MsjVacio) fIn.readObject();
            if (msj.getTipo() != TipoMensaje.MSJ_CERRAR_CONEXION) {
                ClienteLogger.logError("El receptor no ha indicado el cierre de la conexión.");
            }
            fOut.close();
            fIn.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
