package servidor.logic;

import java.util.List;

public class Usuario {
    private String id;
    private List<String> ficheros;
    private boolean conectado;

    public Usuario(String id, List<String> ficheros) {
        this.id = id;
        this.ficheros = ficheros;
        this.conectado = false;     //Por defecto no conectado
    }

    public String getId() {
        return id;
    }

    public List<String> getFicheros() {
        return ficheros;
    }

    public boolean getConectado() {
        return conectado;
    }

    public void conectar() {
        conectado = true;
    }
}
