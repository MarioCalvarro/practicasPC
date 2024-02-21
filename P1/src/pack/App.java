package pack;
import java.util.Random;

public final class App {

    private static void parte1() {
        Random rng = new Random();
        int N = 5;
        Thread[] hilos = new Thread[N];
        for (int i = 0; i < N; i++) {
            hilos[i] = new MyHilo(i, rng); 
            hilos[i].start();
        }

        for (Thread t : hilos) {
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("Terminada la ejecucción!");
    }

    private static void parte2() {
        Entero v = new Entero(0);
        int N = 6;
        Thread[] hilos = new Thread[N];
        for (int i = 0; i < N; i++) {
        	boolean par = i % 2 == 0;
            hilos[i] = new MyHilo2(par, v); 
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

    private static void parte3() {
        int N = 3;
        Thread[] hilos = new Thread[N];
        int[][] i1 = {{1, 2, 3}, {4, 5, 6}, {7, 8, 9}};
        int[][] i2 = {{9, 8, 7}, {6, 5, 4}, {3, 2, 1}};
        int[][] ir = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};

        Matriz mat1 = new Matriz(N, i1);
        Matriz mat2 = new Matriz(N, i2);
        Matriz res = new Matriz(N, ir);
        for (int i = 0; i < N; i++) {
            hilos[i] = new MyHilo3(i, mat1, mat2, res);
            hilos[i].start();
        }

        for (Thread t : hilos) {
            try {
                t.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        System.out.println("El valor final del producto es\n" + res.toString());
    }

	public static void main(String[] args) {
        // parte1();
        // parte2();
        parte3();
	}

}
