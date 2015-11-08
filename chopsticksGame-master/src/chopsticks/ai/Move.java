package chopsticks.ai;

/**
 * Created by kurt on 11/7/15.
 * This represents a turn
 */
public class Move {
	public boolean cRight;
	public boolean pRight;
	public int swapNum=0; // is swap is non-zero, swap to cRight from opposite

	public Move(boolean cR, boolean pR) {
		cRight = cR;
		pRight = pR;
	}

	public Move(boolean cR, boolean pR, int num) {
		cRight = cR;
		pRight = pR;
		swapNum = num;
	}
}
