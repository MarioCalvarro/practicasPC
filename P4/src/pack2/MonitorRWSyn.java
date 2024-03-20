package pack2;

public class MonitorRWSyn implements MonitorRW {
    int nr = 0, nw = 0;

    @Override
    public synchronized void request_read() {
        while (nw > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
    public synchronized void request_write() {
        while (nr > 0 || nw > 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        nw += 1;
    }

    @Override
    public synchronized void release_write() {
        nw -= 1;
        notifyAll();
    }
}
