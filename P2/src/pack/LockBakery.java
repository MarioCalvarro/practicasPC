package pack;

public class LockBakery implements Lock {
	private int N;
	private Entero[] turn;
	
	private int maxTurn() {
		int maxV = turn[0].getValue();
		for(int i = 1; i < N; i++) {
			if (turn[i].getValue() > maxV) {
				maxV = turn[i].getValue();
			}
		}
		return maxV;
	}

	LockBakery(int N) {
		this.N = N;
		this.turn = new Entero[N];
		for(int i = 0; i < N; i++) {
			turn[i] = new Entero(0);
		}
	}

	@Override
	public void takeLock(int i) {
		turn[i].setValue(1);
		turn[i].setValue(1 + maxTurn());
		for(int j = 0; j < N; j++) {
			if(j != i) {
				while(turn[j].getValue() != -1 && 
					  (turn[i].getValue() > turn[j].getValue() || 
					  (turn[i].getValue() == turn[j].getValue() && i > j))) {}
			}
		}
	}

	@Override
	public void realeaseLock(int i) {
		turn[i].setValue(-1);
	}

}
