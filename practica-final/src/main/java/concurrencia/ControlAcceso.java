package concurrencia;

public interface ControlAcceso {
	public void request_read();
	public void release_read();

	public void request_write();
	public void release_write();
}
