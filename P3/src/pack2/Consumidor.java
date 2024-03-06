package pack2;

import java.util.concurrent.Semaphore;

public class Consumidor extends Thread {
    private static int max_it = 10;
	private int id;
	private Producto p;
    private Almacen alm;
	private Semaphore empty;
	private Semaphore full;

	Consumidor(int id, Almacen alm, Semaphore empty, Semaphore full) {
		this.id = id;
        this.alm = alm;
		this.empty = empty;
		this.full = full;
	}
	
	public void run() {
        int i = 0;
        while (i < max_it) {
            try {
				full.acquire();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
            p = alm.extraer();
            empty.release();
            System.out.println("El consumidor " + id + " ha recibido el producto " + p);
            i += 1;
        }
	}
}
