package servidor.logic;

import java.io.Serializable;
import java.util.Set;

public class Usuario implements Serializable {
    private String id;
    private Set<String> ficheros;
    private boolean conectado;

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
}
