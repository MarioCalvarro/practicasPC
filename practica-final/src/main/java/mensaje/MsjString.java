package mensaje;

//Tipos posibles: MSJ_CONEXION, MSJ_CONF_CONEXION, MSJ_PEDIR_FICHERO
public class MsjString extends Mensaje {
    private String contenido;

    public MsjString(TipoMensaje tipo, String str) {
        msj = tipo;
        contenido = str;
    }

    public String getContenido() {
        return contenido;
    }
}
