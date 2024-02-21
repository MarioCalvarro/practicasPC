package pack;

public class Booleano {
	// True: Turno par. False: Impar
	private volatile boolean value;

	Booleano(boolean value) {
		this.value = value;
	}

	public void changeValue() {
		value = !value;
	}

	public boolean getValue() {
		return value;
	}
}
