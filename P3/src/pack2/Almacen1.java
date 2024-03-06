package pack2;

public class Almacen1 implements Almacen {
	
	private Producto p;
	
	@Override
	public void almacenar(Producto producto) {
		this.p = producto;
	}

	@Override
	public Producto extraer() {
		return p;
	}

}
