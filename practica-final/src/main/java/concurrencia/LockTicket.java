package concurrencia;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements Lock {
    //TODO
    private int MAX_THREADS = 100;
    private AtomicInteger number;
    private volatile int next;      //TODO: Volatile?

    public LockTicket() {
        number = new AtomicInteger(1);
        next = 1;
    }

    @Override
    public void takeLock() {
          Integer turno = number.getAndIncrement();
          if (turno == MAX_THREADS) {           //Resta el que ha llegado al "overflow"
              number.addAndGet(-MAX_THREADS);
              //TODO: Restar n a turn?
          }
          else if (turno > MAX_THREADS) {       //El resto que se han pasado solo se actualizan a sí mismos
              turno -= MAX_THREADS;
          }
          while (turno != next) {}
    }

    @Override
    public void releaseLock() {
        next = next % MAX_THREADS + 1;
    }
}
