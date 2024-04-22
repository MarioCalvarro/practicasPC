package concurrencia;

//TODO: Cambiar por uno que no necesite el número de procesos
public class LockRompeEmpate implements Lock {
    private int N;
    private Entero[] in;
    private Entero[] last;

    public LockRompeEmpate(int N) {
        //Número máximo de clientes conectados al servidor
        this.N = N;
        in = new Entero[N];
        last = new Entero[N];
        for (int i = 0; i < N; i++) {
            in[i] = new Entero(-1);
            last[i] = new Entero(-1);
        }
    }

    @Override
    public void takeLock(int i) {
        for (int j = 0; j < N; j++) {
            last[j].setValue(i);
            in[i].setValue(j);
            for (int k = 0; k < N; k++) {
                if (i != k) {
                    while (in[k].getValue() >= in[i].getValue() && last[j].getValue() == i) {
                        // Wait si el proceso k esta en una etapa de mayor número
                        // y el proceso i fue el último en entrar en esa etapa
                    }
                }
            }
        }
    }

    @Override
    public void releaseLock(int i) {
        in[i].setValue(-1);
    }
}
