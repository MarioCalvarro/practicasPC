package concurrencia;

public interface Lock {
    public void takeLock();

    public void releaseLock();
}
