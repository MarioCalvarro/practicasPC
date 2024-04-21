# Protocolo comunicación Cliente-Servidor

## Se inicia el servidor (S).

1. Crea el ServerSocket.
2. Espera en un bucle infinito con .accept().

## El cliente (C) se inicia.

1. Pantalla de bienvenida para el usuario.
2. Usuario introduce su nombre.
3. Se intenta conectar a (S) con ese nombre de usuario.
    - Si falla, se cierra el programa.

## El cliente consigue conectarse.

1. (C) Crea su propio ServerSocket por si tiene que enviar archivos a otros
   clientes.
2. (S) Crea un nuevo OyenteCliente (OC).
3. (C) Crea un nuevo OyenteServidor (OS).
4. Se crea el socket entre (OC) y (OS).
5. (C) envía MSJ_CONEXION(nombre de usuario) a (S).
6. (S) envía MSJ_CONF_CONEXION() a (C).
7. (S) actualiza la tabla de usuarios(lista de usuarios, información
   que tiene cada uno, si están conectados).
8. En (C) se le muestra al usuario una lista de acciones posibles:
    - Consultar información disponible en el sistema.
    - Descargar información deseada.

## (C) solicita información al servidor

1. (C) manda MSJ_LU() a (S).
2. (S) manda MSJ_CONF_LU(lista de usuarios) a (C).
3. (C) muestra al cliente una lista de ficheros disponibles.

## (C) solicita descargar. Protocolo P2P: (Receptor (R) Emisor (E) Servidor(S))

1. El usuario introduce el nombre del fichero que desea.
1. (R) manda MENSAJE_PEDIR_FICHERO(nombre del fichero) a (S).
    - Si el fichero no esta disponible: (S) manda MENSAJE_FICHERO_INEX a (R).
    - (R) se lo indica al usuario.
3. (S) manda MENSAJE_PEDIR_FICHERO(nombre del fichero) a (E).
4. (E) se queda esperando en el accept() de su ServerSocket.
4. (E) manda MENSAJE_PREPARADO_CS(ip, puerto) a (S).
5. (S) manda MENSAJE_PREPARADO_SC(ip, puerto) a (R).
   En este punto, el servidor se olvida de esta conexión.
6. (R) crea el socket asociado al ServerSocket de (E) con el ip y el puerto.
7. (E) manda MENSAJE_INICIO_EMISION_FICHERO() a (R).
8. (E) comienza a enviar por tramos el contenido del fichero a (R).
9. (E) acaba de enviar el fichero y manda MSJ_FIN_EMISION_FICHERO(nombre del
   fichero) a (R).
10. (R) manda MSJ_CERRAR_CONEXION() a (E) y cierra la conexión.
11. (R) manda MSJ_FIN_EMISION_FICHERO(nombre del fichero) a (S).
12. (S) actualiza la base de datos.

## Fin de sesión

1. (C) envía MSJ_CERRAR_CONEXION a (S).
2. (S) actualiza la tabla de usuarios.
