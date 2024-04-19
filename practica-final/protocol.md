# Protocolo comunicación Cliente-Servidor
1. Se inicia el servidor (S).
    - Crea el ServerSocket
    - Espera en un bucle infinito con .accept()
2. El cliente (C1) se inicia.
    - Pantalla de bienvenida para el usuario
    - Usuario introduce su nombre
    - Se intenta conectar al servidor con ese nombre de usuario
        -- Si falla, se cierra el programa
3. El cliente consigue conectarse
    - (C) Crea su propio ServerSocket por si tiene que enviar archivos a otros
      clientes
    - (S) Crea un nuevo OyenteCliente
    - (C) Crea un nuevo OyenteServidor
    - C a S: MSJ_CONEXION
    - S a C: MSJ_CONF_CONEXION


# Protocolo P2P: (Receptor (R) Emisor (E) Servidor(S))
1.(R) manda MENSAJE_PEDIR_FICHERO (nombre del fichero) a (S).
2.(S) manda MENSAJE_PEDIR_FICHERO (nombre del duchero) a (E).
3.(E) se queda esperando en el accept() de su ServerSocket.
4.(E) manda MENSAJE_PREPARADO_CS a (S).
5.(S) manda MENSAJE_PREPARADO_SC (ip, puerto) a (R).
6.(R) crea el socket asociado al ServerSocket de (E) con el ip y el puerto.
    
    
  