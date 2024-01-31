package pack;
import java.lang.Thread;

public class MyHilo3 extends Thread {
    private int row;
    private Matriz m1, m2, res;

    MyHilo3(int row, Matriz m1, Matriz m2, Matriz res) {
        this.row = row;
        this.m1 = m1;
        this.m2 = m2;
        this.res = res;
    }

    public void run() {
        for (int ii = 0; ii < m1.size(); ii++) {
            res.setValue(escalarProd(m1.getRow(row), m2.getCol(ii), m1.size()), row, ii);
        }
    }

    private int escalarProd(int[] v1, int[] v2, int n) {
        int res = 0;
        for (int i = 0; i < n; i++) {
            res += v1[i] * v2[i]; 
        }
        return res;
    }
}
