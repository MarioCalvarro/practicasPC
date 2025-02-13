package cliente.logic;

import cliente.ui.ClienteLogger;
import cliente.ui.ControlPrint;
import mensaje.Mensaje;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;

import java.io.*;
import java.net.Socket;

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
        } catch (IOException e) {
            ControlPrint.logError("Error al abrir los flujos para emitir el fichero '" + archivo + "'.");
            cerrarConexion();
            return;
        }

        try {
            fileIn = new FileInputStream(Cliente.RUTA_FICHEROS + archivo);
        } catch (FileNotFoundException e) {
            ControlPrint.logError("Error al abrir el archivo. ");
            cerrarConexion();
            return;
        }

        try {
            fOut.writeObject(new MsjVacio(TipoMensaje.MSJ_INICIO_EMISION_FICHERO));
            fOut.flush();
        } catch (IOException e) {
            ControlPrint.logError("Error al escribir el mensaje de inicio de emisión del fichero '" + archivo + "'.");
            cerrarConexion();
            return;
        }

        // Crear buffer para lectura del archivo
        byte[] buffer = new byte[8192];
        int bytesRead;

        // Leer el archivo y enviarlo por el socket
        try {
            while ((bytesRead = fileIn.read(buffer)) > 0) {
                fOut.write(buffer, 0, bytesRead);
                fOut.flush();
            }
            //Cerrar el output para que el receptor no se quede colgado
            cs.shutdownOutput();
            fileIn.close();
        } catch (IOException e) {
            ControlPrint.logError("Error al enviar un tramo del fichero '" + archivo + "'.");
            cerrarConexion();
            return;
        }
        ControlPrint.log("Enviado el fichero '" + archivo + "'.");

        Mensaje msj;
        try {
            //Mensaje de fin de cierre de emisión
            msj = (MsjVacio) fIn.readObject();
        } catch (ClassNotFoundException | IOException e) {
            ControlPrint.logError("Error al recibir el mensaje de cierre de conexión con el receptor del fichero '" + archivo + "'.");
            cerrarConexion();
            return;
        }
        if (msj.getTipo() != TipoMensaje.MSJ_CERRAR_CONEXION) {
            ControlPrint.logError("El receptor no ha indicado el cierre de la conexión.");
        }
        try {
            cs.close();
            fIn.close();
        } catch (IOException e) {
            ControlPrint.logError("Error al cerrar el thread de emisión del fichero '" + archivo + "'.");
        }
    }

    private void cerrarConexion() {
        try {
            cs.close();
            fileIn.close();
            fOut.close();
            fIn.close();
        } catch (IOException e) {
            ControlPrint.logError("Error al cerrar el thread de emisión del fichero '" + archivo + "'.");
        }
    }
}
