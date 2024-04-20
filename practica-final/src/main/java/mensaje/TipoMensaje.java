package mensaje;

//TODO: El servidor crea el socket entre los clientes?
public enum TipoMensaje {
    //Envía nombre usuario (String)
    MSJ_CONEXION,

    //No envía nada
    MSJ_CONF_CONEXION,

    //No envía nada
    MSJ_LU,     //LU: lista usuarios

    //Envía la lista de usuarios (tabla de información)
    MSJ_CONF_LU,
    
    //Fichero inexistente
    MSJ_FICH_INEX,

    //Envía nombre del fichero (String)
    MSJ_PEDIR_FICHERO,

    //No envía nada
    MSJ_INICIO_EMISION_FICHERO,

    //Envía nombre de fichero (String)
    MSJ_FIN_EMISION_FICHERO,
    //El fichero se enviará por tramos del tamaño de cierto buffer. Los
    //dos anteriores mensajes marcaran el comienzo y final de esta
    //transferencia

    //Envía el nombre del fichero preparado, su direccion IP y el puerto (String)
    MSJ_PREPARADO_CS,       //CS: Cliente-Servidor

    //Envía la dirección IP y el puerto del emisor (String)
    MSJ_PREPARADO_SC,

    //No envía nada
    MSJ_CERRAR_CONEXION,
}
