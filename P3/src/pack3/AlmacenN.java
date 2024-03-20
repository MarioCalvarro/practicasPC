package pack3;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
public class AlmacenN implements Almacen {	
	private List<Producto> lista;
	private Semaphore e = new Semaphore(1, true);
	private Semaphore r = new Semaphore(0, true);
	private Semaphore w = new Semaphore(0, true);
    private int nw = 0, nr = 0, dr = 0, dw = 0;

	AlmacenN (int N) {
		lista = new ArrayList<Producto>(N);
		for (int i = 0; i < N; i++) {
			lista.add(null);
		}
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
    
    //Incrementar contadores
    public void incNw() {
        nw += 1;
    }

    public void incNr() {
        nr += 1;
    }

    public void incDw() {
        dw += 1;
    }

    public void incDr() {
        dr += 1;
    }

    //Decrementar contadores
    public void decNw() {
        nw -= 1;
    }

    public void decNr() {
        nr -= 1;
    }

    public void decDw() {
        dw -= 1;
    }

    public void decDr() {
        dr -= 1;
    }

    //Devolver contadores
    public int getNw() {
        return nw;
    }

    public int getNr() {
        return nr;
    }

    public int getDw() {
        return dw;
    }
	
    public int getDr() {
        return dr;
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
