package concurrencia;

//TODO: Cambiar a reentrantlock
public class MonitorRW implements ControlAcceso {
    private volatile int nr = 0, nw = 0;

    @Override
    public synchronized void request_read() throws InterruptedException {
        while (nw > 0) {
            wait();
        }
        nr += 1;
    }

    @Override
    public synchronized void release_read() {
        nr -= 1;
        if (nr == 0) {
            notifyAll();
        }
    }


    @Override
    public synchronized void request_write() throws InterruptedException {
        while (nr > 0 || nw > 0) {
            wait();
        }
        nw += 1;
    }

    @Override
    public synchronized void release_write() {
        nw -= 1;
        notifyAll();
    }
}
