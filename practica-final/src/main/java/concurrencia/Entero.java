package concurrencia;

public class Entero {
    private volatile int value;

    Entero(int value) {
        this.value = value;
    }

    public void incrementar() {
        value++;
    }

    public void decrementar() {
        value--;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return String.valueOf(value);
    }
}
