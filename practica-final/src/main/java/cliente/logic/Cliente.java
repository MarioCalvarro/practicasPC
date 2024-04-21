package cliente.logic;

import mensaje.MsjString;
import mensaje.MsjUsuario;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;
import servidor.logic.Servidor;
import servidor.logic.Usuario;

import javax.management.RuntimeErrorException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Scanner;

//Poner bien los lock de fin y fout
//Poner bien los puertos
//Mirar cómo comunicar que se ha terminado la conexion al oyente
//Manejar bien las excpecptiones

public class Cliente {
    private String nombre;
    private ControlOutput fOut;
    private boolean conexionTerminada;
    private Usuario usuario;
    private Socket cs;


    public Cliente(String nombre) throws Exception {
        this.nombre = nombre;
        this.conexionTerminada = false;
        this.usuario = new Usuario(nombre, new HashSet<String>());
        cs = new Socket("localhost", Servidor.PORT_NUMBER);
        ObjectOutputStream output = new ObjectOutputStream(cs.getOutputStream());
        //Todavía no está compartido
        output.writeObject(new MsjUsuario(TipoMensaje.MSJ_CONEXION, usuario)); output.flush();

        fOut = new ControlOutput(output);
        OyenteServidor hc = new OyenteServidor(nombre, cs, fOut);
        hc.start();
    }

    public void consultarInformacion() throws IOException, ClassNotFoundException {
        fOut.escribir(new MsjVacio(TipoMensaje.MSJ_LU));
    }

    public void descargarInformacion(String fichero) throws IOException {
        fOut.escribir(new MsjString(TipoMensaje.MSJ_PEDIR_FICHERO, fichero));

    }

    public void finalizarConexion() throws IOException {
        fOut.escribir(new MsjVacio(TipoMensaje.MSJ_CERRAR_CONEXION));
        cs.close();
        conexionTerminada = true;
    }

    public String getNombre() {
        return nombre;
    }
}
