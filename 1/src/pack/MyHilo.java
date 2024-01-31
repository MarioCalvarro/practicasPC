package pack;
import java.lang.Thread;
import java.util.Random;

public class MyHilo extends Thread {
    private int id;
    private Random rng;
    MyHilo(int id, Random rng) {
        this.id = id;
        this.rng = rng;
    }

    public void run() {
        int espera = rng.nextInt(4) + 1;
        System.out.println("Inicia el hilo " + id + " durante " + espera + " segundos");
        try {
			Thread.sleep(espera * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
        System.out.println("Termina el hilo " + id);
    }
}
