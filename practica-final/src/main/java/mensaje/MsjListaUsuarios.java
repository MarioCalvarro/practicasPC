package mensaje;

public class MsjListaUsuarios extends Mensaje {
    private ListaUsuarios contenido;

    public MsjListaUsuarios(TipoMensaje tipo, ListaUsuarios lista) {
        msj = tipo;
        contenido = lista;
    }

    public ListaUsuarios getContenido() {
        return contenido;
    }
}
