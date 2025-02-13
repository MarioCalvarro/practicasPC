package pack2;

public interface Almacen {
	/**
	* Almacena (como ultimo) un producto en el almacen. Si no hay
	* hueco el proceso que ejecute el método bloqueará hasta que lo
	* haya.
	*/
	public void almacenar(Producto producto);
	/**
	* Extrae el primer producto disponible. Si no hay productos el
	* proceso que ejecute el método bloqueará hasta que se almacene un
	* dato.
	*/
	public Producto extraer();
}
