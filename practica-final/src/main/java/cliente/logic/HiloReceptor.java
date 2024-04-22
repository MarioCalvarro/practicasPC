package cliente.logic;

import mensaje.Mensaje;
import mensaje.MsjString;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import cliente.ui.ClienteLogger;

public class HiloReceptor extends Thread {
    private static final int NUMERO_HILO = 2;
    private String archivo;
    private Socket cs;
    private ObjectInputStream fIn;
    private FileOutputStream fileOutputStream;
    private ObjectOutputStream fOut;
    private ControlOutput controlOutput;

    public HiloReceptor(String archivo, String ip, String puerto, ControlOutput cO) throws NumberFormatException, UnknownHostException, IOException {
        cs = new Socket(ip, Integer.parseInt(puerto));
        this.archivo = archivo;
        fileOutputStream = new FileOutputStream(Cliente.RUTA_FICHEROS + archivo);
        this.controlOutput = cO;

        fIn = new ObjectInputStream(cs.getInputStream());
        fOut = new ObjectOutputStream(cs.getOutputStream());
        this.start();
    }

    @Override
    public void run() {
        // Crear buffer para escritura del archivo
        byte[] buffer = new byte[1024];
        int bytesRead;

        // Leer datos del socket y escribirlos en el archivo
        try {
            Mensaje inicio = (MsjVacio) fIn.readObject();
            if (inicio.getTipo() != TipoMensaje.MSJ_INICIO_EMISION_FICHERO) {
                //TODO
                throw new Exception();
            }

            while ((bytesRead = fIn.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }
            fileOutputStream.close();

            ClienteLogger.log("Recibido el fichero '" + archivo + "'.");
            fOut.writeObject(new MsjVacio(TipoMensaje.MSJ_CERRAR_CONEXION));
            fOut.flush();
            fIn.close();
            fOut.close();
            cs.close();

            controlOutput.escribir(NUMERO_HILO,new MsjString(TipoMensaje.MSJ_FIN_EMISION_FICHERO, archivo));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
