package servidor.logic;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import mensaje.*;

class OyenteCliente extends Thread {
    private BaseDatos baseDatos;
    private TablaFlujos flujos;
    private ObjectInputStream fIn;
    private ObjectOutputStream fOut;
    private String id;
    private int numThread;

    public OyenteCliente(Socket s, int numThread, BaseDatos baseDatos, TablaFlujos flujos)
    {
        this.numThread = numThread;
        this.baseDatos = baseDatos;
        this.flujos = flujos;
        try {
            fIn = new ObjectInputStream(s.getInputStream());
            fOut = new ObjectOutputStream(s.getOutputStream());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void establecerConexion() {
        MsjUsuario msj;
        try {
            msj = (MsjUsuario) fIn.readObject();
            if (msj.getTipo() != TipoMensaje.MSJ_CONEXION) {
                //TODO: Error
                throw new Exception();
            }
            this.id = msj.getContenido();
            //TODO: Logger
            System.out.println("Solicitud de conexión de: " + this.id);
            System.out.println("Enviando confirmación de conexión");
            fOut.writeObject(new MsjVacio(TipoMensaje.MSJ_CONF_CONEXION));

            //Actualizamos las tablas
            this.flujos.nuevoHilo(numThread, id, fIn, fOut);
            //TODO: Actualizar usuario
            this.baseDatos.conexionUsuario();
        } catch (Exception e) {
            //TODO: Mejorar salida
            interrupt();
        }
    }

    @Override
    public void run() {
        try {
            establecerConexion();
        } catch (Exception e) {
            return;
        }
    }
}
