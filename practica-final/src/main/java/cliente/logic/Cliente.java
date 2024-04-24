package cliente.logic;

import mensaje.MsjString;
import mensaje.MsjUsuario;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;
import servidor.logic.Servidor;
import servidor.logic.Usuario;

import java.io.File;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashSet;
import java.util.Set;

public class Cliente {
    protected static final String RUTA_FICHEROS = "./usuarios/";
    private static final int NUMERO_HILO = 0;

    private String nombre;
    private ControlOutput fOut;
    private OyenteServidor hc;
    private Usuario usuario;
    private GestionFicheros ficheros;
    private Socket cs;

    public Cliente(String nombre) throws IOException, ClassNotFoundException {
        this.nombre = nombre;
        Set<String> ficherosUsuario = getFicherosUsuario(nombre);
        this.usuario = new Usuario(nombre, ficherosUsuario);
        this.ficheros = new GestionFicheros(ficherosUsuario);

        cs = new Socket("localhost", Servidor.PORT_NUMBER);
        ObjectOutputStream output = new ObjectOutputStream(cs.getOutputStream());
        //Todavía no está compartido
        output.writeObject(new MsjUsuario(TipoMensaje.MSJ_CONEXION, usuario));
        output.flush();

        fOut = new ControlOutput(output);
        hc = new OyenteServidor(nombre, cs, fOut, ficherosUsuario);
        hc.start();
    }

    private Set<String> getFicherosUsuario(String nombreUser) {
        File carpetaUsuario = new File(RUTA_FICHEROS + nombreUser);
        if (!carpetaUsuario.exists() || !carpetaUsuario.isDirectory()) {
            carpetaUsuario.mkdirs();
            return new HashSet<>();
        }
        Set<String> res = new HashSet<>();
        File[] archivos = carpetaUsuario.listFiles();
        for (File arc : archivos) {
            res.add(arc.getName());
        }
        return res;
    }

    public void consultarInformacion() throws IOException {
        fOut.escribir(NUMERO_HILO, new MsjVacio(TipoMensaje.MSJ_LU));
    }

    public void descargarInformacion(String fichero) throws IOException {
        fOut.escribir(NUMERO_HILO, new MsjString(TipoMensaje.MSJ_PEDIR_FICHERO, fichero));
    }

    public void finalizarConexion() throws IOException, InterruptedException {
        fOut.escribir(NUMERO_HILO, new MsjVacio(TipoMensaje.MSJ_CERRAR_CONEXION));
        cs.close();
        hc.interrupt();
        hc.join();
    }

    public String getNombre() {
        return nombre;
    }
}
