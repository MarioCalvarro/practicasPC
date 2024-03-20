package pack2;

public class Lector extends Thread {
    private static int max_it = 10;
	private int id;
    private Almacen alm;

	Lector(int id, Almacen alm) {
		this.id = id;
        this.alm = alm;
	}
	
	public void run() {
		int i = 0;
        while (i < max_it) {
            Producto p = alm.leer(id);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            i += 1;
        }
    }
}
