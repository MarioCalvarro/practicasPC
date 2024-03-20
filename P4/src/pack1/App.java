package pack1;

public class App {
	private static void parte1() {
		int N = 10;
		Thread[] hilos = new Thread[2 * N];
        Monitor m = new Monitor1();
		for (int i = 0; i < 2 * N; i++) {
			boolean par = i % 2 == 0;
			hilos[i] = new MyHilo(i, par, m);
			hilos[i].start();
		}

		for (Thread t : hilos) {
			try {
				t.join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Terminada la ejecucción, v es " + m.getValue());
	}

	public static void main(String[] args) {
		parte1();
	}
}
