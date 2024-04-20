package cliente.logic;

import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

import mensaje.Mensaje;
import mensaje.MsjUsuario;
import mensaje.MsjVacio;
import mensaje.TipoMensaje;

public class HiloEmisor extends Thread{
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
	
	private void run() {
		cs = ss.accept();
		try {
            fOut = new ObjectOutputStream(cs.getOutputStream());
            fIn = new ObjectInputStream(cs.getInputStream());

    		fileInputStream = new FileInputStream(archivo);
        } catch (Exception e) {
            e.printStackTrace();
        }
		fOut.writeObject(new MsjVacio(TipoMensaje.MSJ_INICIO_EMISION_FICHERO));
	    fOut.flush();
	    
	 // Crear buffer para lectura del archivo
        byte[] buffer = new byte[1024];
        int bytesRead;
        
        
        // Leer el archivo y enviarlo por el socket
        while ((bytesRead = fIn.read(buffer)) != -1) {
            fOut.write(buffer, 0, bytesRead);
        }
        
       
        fOut.writeObject(new MsjVacio(TipoMensaje.MSJ_FIN_EMISION_FICHERO));
	    fOut.flush();
	    
	    MsjVacio msj = (MsjVacio) fIn.readObject();
	    fOut.close();
	    fIn.close();
        fileInputStream.close();
	    
	    
	}
}
