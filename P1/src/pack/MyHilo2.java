package pack;
import java.lang.Thread;
import java.util.Random;

public class MyHilo2 extends Thread {
    private boolean par;
    private Entero v;

    private static int N = 100;

    MyHilo2(boolean par, Entero v) {
        this.par = par;
        this.v = v;
    }

    public void run() {
        if (par) {
            for (int i =  0; i < N; i++) {
                v.incrementar();
            }  
        }
        else {
            for (int i =  0; i < N; i++) {
                v.decrementar();
            }  
        }
    }
}
