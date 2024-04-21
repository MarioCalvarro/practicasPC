package concurrencia;

public interface Lock {
    public void takeLock(int i);

    public void releaseLock(int i);
}
