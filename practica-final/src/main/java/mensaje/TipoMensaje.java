package mensaje;

public enum TipoMensaje {
    //Acciones en el servidor
    BUSCAR,
    INICIAR_CONEXION_P2P,
    //Acciones en el cliente
    ENVIAR,
    //TODO: Ambos? (Cada uno hace cosas distintas)
    FIN_SESION,
}
