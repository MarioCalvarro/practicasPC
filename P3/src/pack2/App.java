package pack2;

import java.util.concurrent.Semaphore;

public class App {

	public static void main(String[] args) {
		int num_threads = 3;
		int num_productos = 5;
		Almacen alm = new AlmacenN();
		Thread[] productores = new Thread[num_threads];
		Thread[] consumidores = new Thread[num_threads];
		Semaphore empty = new Semaphore(num_productos, true);
		Semaphore full = new Semaphore(0, true);
		for (int i = 0; i < num_threads; i++) {
			productores[i] = new Productor(i, alm, empty, full);
			consumidores[i] = new Consumidor(i, alm, empty, full);
			productores[i].start();
			consumidores[i].start();
		}
		
		for (int i = 0; i < num_threads; i++) {
			try {
				productores[i].join();
				consumidores[i].join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Terminada la ejecucción");
	}

}
