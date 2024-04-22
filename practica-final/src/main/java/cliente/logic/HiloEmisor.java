package cliente.logic;

import mensaje.Mensaje;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import cliente.ui.ClienteLogger;

public class HiloEmisor extends Thread {
    private Socket cs;
    private FileInputStream fileIn;
    private ObjectOutputStream fOut;
    private ObjectInputStream fIn;
    private String archivo;

    public HiloEmisor(String archivo, Socket cs) {
        this.archivo = archivo;
        this.cs = cs;
        this.start();
    }

    @Override
    public void run() {
        try {
            fOut = new ObjectOutputStream(cs.getOutputStream());
            fIn = new ObjectInputStream(cs.getInputStream());

            fileIn = new FileInputStream(Cliente.RUTA_FICHEROS + archivo);
            fOut.writeObject(new MsjVacio(TipoMensaje.MSJ_INICIO_EMISION_FICHERO));
            fOut.flush();

            // Crear buffer para lectura del archivo
            byte[] buffer = new byte[8192];
            int bytesRead;

            // Leer el archivo y enviarlo por el socket
            while ((bytesRead = fileIn.read(buffer)) > 0) {
                fOut.write(buffer, 0, bytesRead);
                fOut.flush();
            }
            ClienteLogger.log("Enviado el fichero '" + archivo + "'.");

            Mensaje msj = (MsjVacio) fIn.readObject();
            if (msj.getTipo() != TipoMensaje.MSJ_CERRAR_CONEXION) {
                ClienteLogger.logError("El receptor no ha indicado el cierre de la conexión.");
            }
            fOut.close();
            fIn.close();
            fileIn.close();
            cs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
