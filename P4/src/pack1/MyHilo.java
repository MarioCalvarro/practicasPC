package pack1;

import java.lang.Thread;

public class MyHilo extends Thread {
	private int id;
	private boolean par;
	private Monitor m;

	private static int N = 1000;

	MyHilo(int id, boolean par, Monitor m) {
		this.id = id;
		this.par = par;
        this.m = m;
	}

	public void run() {
        int i = 0;
		if (par) {
			while (i < N) {
                m.incrementar();
				i += 1;
			}
		} else {
			while (i < N) {
                m.decrementar();
				i += 1;
			}
		}
        System.out.println("Terminada ejecucción de " + id);
	}
}
