package pack2;

public class App {
	public static void main(String[] args) {
		int num_threads = 100, num_productos = 100;
		MonitorRW m = new MonitorRWSyn();
		Almacen alm = new Almacen(num_productos, m);
		Thread[] escritores = new Thread[num_threads];
		Thread[] lectores = new Thread[num_threads];
		for (int i = 0; i < num_threads; i++) {
			escritores[i] = new Escritor(i, alm);
			lectores[i] = new Lector(i, alm);
			escritores[i].start();
			lectores[i].start();
		}
		
		for (int i = 0; i < num_threads; i++) {
			try {
				escritores[i].join();
				lectores[i].join();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		System.out.println("Terminada la ejecucción");
	}
}
