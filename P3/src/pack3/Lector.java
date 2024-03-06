package pack3;

public class Lector extends Thread {
    private static int max_it = 10;
	private int id;
    private AlmacenN alm;

	Lector(int id, AlmacenN alm) {
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
