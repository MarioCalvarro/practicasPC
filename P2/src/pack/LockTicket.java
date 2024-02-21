package pack;

import java.util.concurrent.atomic.AtomicInteger;

public class LockTicket implements Lock {
    private AtomicInteger number; // Initialize with 0
	private volatile int next;
	private Entero[] turn;

	LockTicket(int N) {
		this.number = new AtomicInteger(1);
		this.next = 1;
		this.turn = new Entero[N];
		for(int i = 0; i < N; i++) {
			turn[i] = new Entero(0);
		}
	}

	@Override
	public void takeLock(int i) {
		//Asignamos su turno
		turn[i].setValue(number.getAndAdd(1));
		//Esperamos a que sea su turno
		while(turn[i].getValue() != next) {}
	}

	@Override
	public void realeaseLock(int i) {
		//Siguiente turno
		next += 1;
	}

}
