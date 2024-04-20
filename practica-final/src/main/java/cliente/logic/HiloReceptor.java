package cliente.logic;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import mensaje.Mensaje;
import mensaje.MsjString;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;

public class HiloReceptor {
	private Socket cs;
	private ObjectInputStream fIn;
	private FileOutputStream fileOutputStream;
	private ObjectOutputStream fOut;
	private String archivo;

	
	public HiloReceptor(String archivo ,String id, String puerto) throws NumberFormatException, UnknownHostException, IOException {
		cs = new Socket(id, Integer.parseInt(puerto));
		try {
            fIn = new ObjectInputStream(cs.getOutputStream());
            fOut = new ObjectOutputStream(cs.getInputStream());
            this.archivo=archivo;
   		 	fileOutputStream = new FileOutputStream(archivo+".txt");
            run();
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	
	private void run() throws ClassNotFoundException, IOException {
		// Crear buffer para escritura del archivo
        byte[] buffer = new byte[1024];
        int bytesRead;
        
        // Leer datos del socket y escribirlos en el archivo
        while ((bytesRead = fIn.read(buffer)) != -1) {
            fileOutputStream.write(buffer, 0, bytesRead);
        } 
        
        fOut.writeObject(new MsjVacio(TipoMensaje. MSJ_CERRAR_CONEXION));
        fOut.flush();
        
        
        //Esto lo tego q mandar al servidor
        fOut.writeObject(new MsjString(TipoMensaje. MSJ_FIN_EMISION_FICHERO, archivo));
        fOut.flush();
        
        fIn.close();
        fileOutputStream.close();
        fOut.close();
        cs.close();
        
        
	}	
	
}
