package pack2;

import java.util.concurrent.Semaphore;

public class Productor extends Thread {
    private static int max_it = 10;
	private int id;
    private Almacen alm;
	private Semaphore empty;
	private Semaphore full;

	Productor(int id, Almacen alm, Semaphore empty, Semaphore full) {
		this.id = id;
        this.alm = alm;
		this.empty = empty;
		this.full = full;
	}
	
	public void run() {
		int i = 0;
        while (i < max_it) {
            Producto p = new Producto(id + "." + i);
            System.out.println("El productor " + id + " ha escrito " + p);
            try {
				empty.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            alm.almacenar(p);
            full.release();
            i += 1;
        }
	}
}
