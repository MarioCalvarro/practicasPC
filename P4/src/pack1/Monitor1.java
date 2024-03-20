package pack1;

public class Monitor1 implements Monitor {
	int contador;
	
	public Monitor1() {
		contador = 0;
	}

    @Override
    public synchronized void incrementar() {
        contador += 1;
    }

    @Override
    public synchronized void decrementar() {
        contador -= 1;
    }

    @Override
    public int getValue() {
        return contador;
    }
}
