package pack;

import java.lang.Thread;

public class MyHilo extends Thread {
	private int id;
	private boolean par;
	private Entero v;
	private Lock l;

	private static int N = 1000;

	MyHilo(int id, boolean par, Entero v, Lock l) {
		this.id = id;
		this.par = par;
		this.v = v;
		this.l = l;
	}

	public void run() {
		if (par) {
			int i = 0;
			while (i < N) {
				l.takeLock(id);
				v.incrementar();
				l.realeaseLock(id);
				i += 1;
			}
		} else {
			int i = 0;
			while (i < N) {
				l.takeLock(id);
				v.decrementar();
				l.realeaseLock(id);
				i += 1;
			}
		}
	}
}
