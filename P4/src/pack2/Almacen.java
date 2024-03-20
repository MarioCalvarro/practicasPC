package pack2;

import java.util.ArrayList;
import java.util.List;

public class Almacen {
    List<Producto> alm;
    MonitorRW m;

    public Almacen(int N, MonitorRW m) {
        alm = new ArrayList<Producto>(N);
        for (int i = 0; i < N; i++) {
            alm.add(null);
        }
        this.m = m;
    }

    public void escribir(Producto p, int pos) {
        m.request_write();
        alm.set(pos, p);
        System.out.println("Escrito el producto " + p);
        m.release_write();
    }

    public Producto leer(int pos) {
        Producto s;
        m.request_read();
        s = alm.get(pos);
        System.out.println("Leído el producto " + s);
        m.release_read();
        return s;
    }
}
