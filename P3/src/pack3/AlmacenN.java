package pack3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
public class AlmacenN implements Almacen {
	
	private List<Producto> lista;
	private Semaphore e = new Semaphore(1, true);
	private Semaphore r = new Semaphore(0, true);
	private Semaphore w = new Semaphore(0, true);

	
	AlmacenN (int N) {
		lista = new ArrayList<Producto>(N);
	}
	
	public void semEAcquire() {
		try {
			e.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void semRAcquire() {
		try {
			r.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void semWAcquire() {
		try {
			w.acquire();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void semERelease() {
		e.release();
	}

	public void semRRelease() {
		r.release();
	}
	
	public void semWRelease() {
		w.release();
	}
	
	@Override
	public void escribir(Producto producto, int pos) {
		lista.set(pos, producto);
	}

	@Override
	public Producto leer(int pos) {
		return lista.get(pos);
	}

}
