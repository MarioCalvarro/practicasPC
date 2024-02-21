package pack;

public class App {

	private static void parte2() {
		Entero v = new Entero(0);
		int N = 10;
		Lock l = new LockBakery(N);
		Thread[] hilos = new Thread[N];
		for (int i = 0; i < N; i++) {
			boolean par = i % 2 == 0;
			hilos[i] = new MyHilo(i, par, v, l);
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
		parte2();
	}

}
