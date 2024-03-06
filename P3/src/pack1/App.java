package pack1;

import java.util.concurrent.Semaphore;

public class App {

	private static void parte1() {
		Entero v = new Entero(0);
		int N = 10;
		Thread[] hilos = new Thread[2 * N];
		Semaphore s = new Semaphore(1, true); // Fair
		for (int i = 0; i < 2 * N; i++) {
			boolean par = i % 2 == 0;
			hilos[i] = new MyHilo1(i, par, v, s);
			hilos[i].start();
		}

		for (Thread t : hilos) {
			try {
				t.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Terminada la ejecucción, v es " + v.getValue());
	}

	public static void main(String[] args) {
		parte1();
	}

}
