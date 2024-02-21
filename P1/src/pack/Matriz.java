package pack;

public class Matriz {
    private int size;
    private int[][] data;

    Matriz(int size, int[][] data) {
        this.size = size;
        this.data = data;
    }

    public int[] getRow(int r) {
        return this.data[r];
    }
    
    public int[] getCol(int c) {
        int[] res = new int[size];
        for (int i = 0; i < size; i++) {
            res[i] = this.data[i][c];
        }
        return res;
    }

    public void setValue(int value, int r, int c) {
        this.data[r][c] = value;
    }

    public int size() {
        return size;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] r : this.data) {
            for (int value : r) {
                sb.append(value).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
