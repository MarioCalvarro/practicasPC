package concurrencia;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket {
    //TODO: Number no tiene que ser atomic integer
    //Usar con desbordamiento
    private AtomicInteger number, next;

    public LockTicket() {
        number = new AtomicInteger(0);
        next = new AtomicInteger(0);
    }

    //@Override
    public void takeLock() {
        int turno = next.getAndIncrement();
        while (turno != number.get()) {
        }
    }

    //@Override
    public void releaseLock() {
        int actual = number.get();
        number.compareAndSet(actual, actual + 1);
    }
}
