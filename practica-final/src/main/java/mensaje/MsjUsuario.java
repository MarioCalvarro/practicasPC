package mensaje;

import servidor.logic.Usuario;

//Tipo: MENSAJE_CONEXION
public class MsjUsuario extends Mensaje {
    private Usuario contenido;

    public MsjUsuario(TipoMensaje tipo, Usuario user) {
        msj = tipo;
        contenido = user;
    }

    public Usuario getContenido() {
        return contenido;
    }
}
