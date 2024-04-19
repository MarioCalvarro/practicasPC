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
