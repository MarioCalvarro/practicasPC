package concurrencia;

import java.util.concurrent.Semaphore;

public class SemaforoRW implements Semaforo {
	private Semaphore e, r, w;
    private int nw, nr, dr, dw;

    public SemaforoRW() {
        e = new Semaphore(1, true);
        r = new Semaphore(0, true);
        w = new Semaphore(0, true);
        nw = 0; nr = 0; dr = 0; dw = 0;
    }

    @Override
    public void request_read() {
        try {
            //Despertar en cadena
            e.acquire();
            if (nw > 0) {
                dr += 1;
                e.release();
                r.acquire();      //Paso de testigo e
            }
            nr += 1;
            if (dr > 0) {
                dr -= 1;
                r.acquire();      //Paso de testigo e
            }
            else {    //Ultimo reader
                e.release();
            }
        } catch (Exception e) {
            //TODO: Error
            throw new RuntimeException("Error");
        }
    }

    @Override
    public void release_read() {
        try {
            e.acquire();
            nr -= 1;
            if (nr == 0 && dw > 0) {
                dw -= 1;
                w.release();      //Paso de testigo e
            }
            else {
                e.release();
            }
        } catch (Exception e) {
            //TODO: Error
            throw new RuntimeException("Error");
        }
    }

    @Override
    public void request_write() {
        try {
            e.acquire();
            if (nr > 0 || nw > 0) {
                dw += 1;
                e.release();
                w.acquire();      //Paso de testigo e
            }
            nw += 1;
            e.release();
        } catch (Exception e) {
            //TODO: Error
            throw new RuntimeException("Error");
        }
    }

    @Override
    public void release_write() {
        try {
        e.acquire();
        nw -= 1;
        if (dr > 0) {
            dr -= 1;
            r.release();
        }
        else if (dw > 0) {
            dw -= 1;
            w.release();
        }
        else {
            e.release();
        }
        } catch (Exception e) {
            //TODO: Error
            throw new RuntimeException("Error");
        }
    }
}
