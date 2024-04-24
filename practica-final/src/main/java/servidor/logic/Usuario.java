package servidor.logic;

import java.io.Serializable;
import java.util.Set;

public class Usuario implements Serializable {
    private String id;
    private Set<String> ficheros;
    private volatile boolean conectado;

    public Usuario(String id, Set<String> ficheros) {
        this.id = id;
        this.ficheros = ficheros;
        this.conectado = false;     //Por defecto no conectado
    }

    public String getId() {
        return id;
    }

    public Set<String> getFicheros() {
        return ficheros;
    }

    public boolean getConectado() {
        return conectado;
    }

    public void conectar() {
        conectado = true;
    }

    public void desconectar() {
        conectado = false;
    }

    public void nuevoFichero(String fichero) {
        ficheros.add(fichero);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Usuario: ").append(id);
        sb.append(". Ficheros: ");
        String espacio = "";
        for (String fichero : ficheros) {
            sb.append(espacio).append(fichero);
            espacio = " ";
        }
        sb.append(". Conectado: ");
        if (conectado) {
            sb.append("Sí.");
        } else {
            sb.append("No.");
        }
        return sb.toString();
    }
}
