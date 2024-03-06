package pack2;

import java.util.ArrayDeque;
import java.util.Queue;

public class AlmacenN implements Almacen {
	
	private Queue<Producto> cola = new ArrayDeque<Producto>();

	@Override
	public void almacenar(Producto producto) {
		cola.add(producto);
	}

	@Override
	public Producto extraer() {
		return cola.remove();
	}

}
