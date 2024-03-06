package pack1;

import java.lang.Thread;
import java.util.concurrent.Semaphore;

public class MyHilo1 extends Thread {
	private int id;
	private boolean par;
	private Entero v;
	private Semaphore s;

	private static int N = 1000;

	MyHilo1(int id, boolean par, Entero v, Semaphore s) {
		this.id = id;
		this.par = par;
		this.v = v;
		this.s = s;
	}

	public void run() {
		if (par) {
			int i = 0;
			while (i < N) {
				try {
					s.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				v.incrementar();
				s.release();
				i += 1;
			}
		} else {
			int i = 0;
			while (i < N) {
				try {
					s.acquire();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				v.decrementar();
				s.release();
				i += 1;
			}
		}
	}
}
