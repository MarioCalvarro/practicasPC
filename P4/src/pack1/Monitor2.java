package pack1;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Monitor2 implements Monitor {
    private int contador;
    private final Lock lock = new ReentrantLock();

    public Monitor2() {
        contador = 0;
    }

    @Override
    public void incrementar() {
        lock.lock();
        contador += 1;
        lock.unlock();
    }

    @Override
    public void decrementar() {
        lock.lock();
        contador -= 1;
        lock.unlock();
    }

    @Override
    public int getValue() {
        return contador;
    }
}
