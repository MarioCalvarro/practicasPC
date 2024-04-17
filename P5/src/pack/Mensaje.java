package pack;

import java.io.Serializable;

public class Mensaje implements Serializable {
    //0: Conexión
    //1: Confirmación conexión
    //2: Cierre conexión
    private int tipo;
    private String contenido;

    public Mensaje(int tipo, String contenido) {
        this.tipo = tipo;
        this.contenido = contenido;
    }

    public int getTipo() {
        return tipo;
    }

    public String getContenido() {
        return contenido;
    }
}
