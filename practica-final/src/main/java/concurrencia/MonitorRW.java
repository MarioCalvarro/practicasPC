package concurrencia;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MonitorRW implements ControlAcceso {
    private volatile int nr = 0, nw = 0;
    private final Lock lock = new ReentrantLock();
    private final Condition condR = lock.newCondition();
    private final Condition condW = lock.newCondition();

    @Override
    public void request_read() throws InterruptedException {
        lock.lock();
        while (nw > 0) {
            condR.wait();
        }
        nr += 1;
        lock.unlock();
    }

    @Override
    public void release_read() throws InterruptedException {
        lock.lock();
        nr -= 1;
        if (nr == 0) {
            condW.signal();
        }
        lock.unlock();
    }

    @Override
    public void request_write() throws InterruptedException {
        lock.lock();
        while (nr > 0 || nw > 0) {
            condW.wait();
        }
        nw += 1;
        lock.unlock();
    }

    @Override
    public void release_write() throws InterruptedException {
        lock.lock();
        nw -= 1;
        condW.signal();
        condR.signal();
        lock.unlock();
    }
}
