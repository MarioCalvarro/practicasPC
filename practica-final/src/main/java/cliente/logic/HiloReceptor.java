package cliente.logic;

import cliente.ui.ClienteLogger;
import mensaje.Mensaje;
import mensaje.MsjString;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.BufferedInputStream;
import java.net.Socket;

public class HiloReceptor extends Thread {
    private int numHilo;
    private String archivo;
    private Socket cs;
    private ObjectInputStream fIn;
    private FileOutputStream fileOutputStream;
    private ObjectOutputStream fOut;
    private ControlOutput controlOutput;


    public HiloReceptor(String id, String archivo, String ip, String puerto, ControlOutput cO, int numHilo) throws IOException {
        cs = new Socket(ip, Integer.parseInt(puerto));
        this.archivo = archivo;
        fileOutputStream = new FileOutputStream(Cliente.RUTA_FICHEROS + id + "/" + archivo);
        this.controlOutput = cO;
        this.numHilo = numHilo;

        fIn = new ObjectInputStream(cs.getInputStream());
        fOut = new ObjectOutputStream(cs.getOutputStream());
        this.start();
    }

    @Override
    public void run() {
        // Leer datos del socket y escribirlos en el archivo
        try {
            Mensaje inicio = (MsjVacio) fIn.readObject();
            if (inicio.getTipo() != TipoMensaje.MSJ_INICIO_EMISION_FICHERO) {
                ClienteLogger.logError("Error al recibir el mensaje de inicio de emisión del fichero '" + archivo + "'.");
                cerrarConexion();
                return;
            }
        } catch (ClassNotFoundException | IOException e) {
            ClienteLogger.logError("Error al leer el mensaje de inicio de emisión de fichero '" + archivo + "'.");
            cerrarConexion();
            return;
        }

        // Crear buffer para escritura del archivo
        int bytesRead;
        byte[] buffer = new byte[1024];
        try {
            InputStream byteIn = new BufferedInputStream(cs.getInputStream());
            while ((bytesRead = fIn.read(buffer)) > 0) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
        } catch (IOException e) {
            ClienteLogger.logError("Error al recibir un tramo del fichero '" + archivo + "'.");
            cerrarConexion();
            return;
        }

        try {
            ClienteLogger.log("Recibido el fichero '" + archivo + "'.");
            fOut.writeObject(new MsjVacio(TipoMensaje.MSJ_CERRAR_CONEXION));
            fOut.flush();
        } catch (IOException e) {
            ClienteLogger.logError("Error al escribir el mensaje de cierre de conexión.");
        }

        try {
            controlOutput.escribir(numHilo, new MsjString(TipoMensaje.MSJ_FIN_EMISION_FICHERO, archivo));
        } catch (IOException e) {
            ClienteLogger.logError("Error al escribir el mensaje de fin de emisión del fichero '" + archivo + "'.");
            cerrarConexion();
            return;
        }
        cerrarConexion();
    }

    private void cerrarConexion() {
        try {
            cs.close();
            fOut.close();
            fIn.close();
            fileOutputStream.close();
        } catch (IOException e) {
            ClienteLogger.logError("Error al cerrar el thread de recepción del fichero '" + archivo + "'.");
        }
    }
}

