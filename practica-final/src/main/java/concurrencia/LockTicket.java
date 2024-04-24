package concurrencia;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket {
    private AtomicInteger number, next;

    public LockTicket(int N) {
       number = new AtomicInteger(0);
       next = new AtomicInteger(0);
    }

    //@Override
    public void takeLock() {
        int turno = next.getAndIncrement();
        while(turno != number.get()){}        
    }

    //@Override
    public void releaseLock(int i) {
        int actual = number.get();
        number.compareAndSet(actual, actual +1);
    }
}
