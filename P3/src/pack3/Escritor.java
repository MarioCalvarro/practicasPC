package pack3;

import java.util.concurrent.Semaphore;

public class Escritor extends Thread {
    private static int max_it = 10;
	private int id;
	private Producto p;
    private AlmacenN alm;

	Escritor(int id, AlmacenN alm) {
		this.id = id;
        this.alm = alm;
	}
	
	public void run() {
        int i = 0;
        while (i < max_it) {
            
            i += 1;
        }
	}
}
