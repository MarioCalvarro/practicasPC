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
            int pos = id;
            //Despertar en cadena
            alm.semEAcquire();
            if (alm.getNw() > 0) {
                alm.incDr();
                alm.semERelease();
                alm.semRAcquire();      //Paso de testigo e
            }
            alm.incNr();
            if (alm.getDr() > 0) {
                alm.decDr();
                alm.semRAcquire();      //Paso de testigo e
            }
            else {    //Ultimo reader
                alm.semERelease();
            }

            System.out.println("El valor leído por el lector " + id + " es " + alm.leer(pos));

            alm.semEAcquire();
            alm.decNr();
            if (alm.getNr() == 0 && alm.getDw() > 0) {
                alm.decDw();
                alm.semWRelease();      //Paso de testigo e
            }
            else {
                alm.semERelease();
            }
            i += 1;
        }
    }
}
