package pack3;

import java.util.concurrent.Semaphore;

public class Escritor extends Thread {
    private static int max_it = 10;
	private int id;
	private Producto p;
    private AlmacenN alm;

	Escritor(int id, AlmacenN alm) {
		this.id = id;
        this.alm = alm;
	}
	
	public void run() {
        int i = 0;
        while (i < max_it) {
            String nombre = "Producto de ".concat(String.valueOf(id));
            p = new Producto(nombre);
            alm.semEAcquire();
            if (alm.getNr() > 0 || alm.getNw() > 0) {
                alm.incDw();
                alm.semERelease();
                alm.semWAcquire();      //Paso de testigo e
            }
            alm.incNw();
            alm.semERelease();

            System.out.println("El escritor " + id + " almacena el valor: '" + nombre + "'");
            alm.escribir(p, id);

            alm.semEAcquire();
            alm.decNw();
            if (alm.getDr() > 0) {
                alm.decDr();
                alm.semRRelease();
            }
            else if (alm.getDw() > 0) {
                alm.decDw();
                alm.semWRelease();
            }
            else {
                alm.semERelease();
            }
            i += 1;
        }
    }
}
