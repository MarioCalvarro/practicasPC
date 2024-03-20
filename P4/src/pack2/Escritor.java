package pack2;

public class Escritor extends Thread {
    private static int max_it = 10;
	private int id;
	private Producto p;
    private Almacen alm;

	Escritor(int id, Almacen alm) {
		this.id = id;
        this.alm = alm;
	}
	
	public void run() {
        int i = 0;
        while (i < max_it) {
            String nombre = "Producto de ".concat(String.valueOf(id)).concat(".").concat(String.valueOf(i));
            p = new Producto(nombre);
            //TODO: Cambiar pos
            alm.escribir(p, id);
            i += 1;
        }
    }
}
