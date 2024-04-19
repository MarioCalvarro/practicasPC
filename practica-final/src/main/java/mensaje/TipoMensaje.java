package mensaje;

public enum TipoMensaje {
    MSJ_CONEXION,
    MSJ_CONF_CONEXION,
    MSJ_LU,     //LU: lista usuarios
    MSJ_CONF_LU,
    MSJ_PEDIR_FICHERO,
    MSJ_EMITIR_FICHERO,
    MSJ_PREPARADO_CS,       //CS: Cliente-Servidor
    MSJ_PREPARADO_SC,
    MSJ_CERRAR_CONEXION,
}
