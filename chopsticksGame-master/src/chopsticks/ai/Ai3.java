package chopsticks.ai;

import chopsticks.Player;

import java.util.ArrayList;

/**
 * Created by kurt on 11/7/15.
 */
public class Ai3 {
	static Player computer;
	static Player player;

	static int compL;
	static int compR;
	static int plR;
	static int plL;
	static Move best_move;
	static int best_score;
	static Move m;

	public static Move computerGo(int cR, int cL, int pR, int pL) {

		computer = new Player();
		computer.left = cL;
		computer.right = cR;

		player = new Player();
		player.left = pL;
		player.right = pR;

		compL = cL;
		compR = cR;
		plR = pR;
		plL = pL;
		minimax(new Game(computer, player), 0, true);
		return m;
	}

	public static int minimax(Game g, int depth, boolean isComputer) {
		if (GameOver(g) || depth == 6) {
			return evaluate(g, depth);
		}
		depth++;
		ArrayList<Move> moves = new ArrayList<>();
		ArrayList<Integer> scores = new ArrayList<>();
		for (Move m : GenerateMoves(g)) {
			Game clone = ApplyMove(m, g, isComputer);
			scores.add(minimax(clone, depth, !isComputer));
			moves.add(m);
		}

		if (!isComputer) {//if player
			int largest = scores.get(0), index = 0;
			for (int i = 1; i < scores.size(); i++) {
				if (scores.get(i) > largest) {
					largest = scores.get(i);
					index = i;
				}
			}
			m = moves.get(index);
			return scores.get(index);
		} else {
			int smallest = scores.get(0), index = 0;
			for (int i = 1; i < scores.size(); i++) {
				if (scores.get(i) < smallest) {
					smallest = scores.get(i);
					index = i;
				}
			}
			return scores.get(index);
		}
	}


	private static boolean GameOver(Game g) {
		if (g.computer.left + g.computer.right < 1) {
			return true;
		} else if (g.player.left + g.player.right < 1) {
			return true;
		} else return false;
	}


	public static int evaluate(Game g, int depth) {
		if (g.computer.left + g.computer.right < 1) {
			return 10 - depth;
		} else if (g.player.left + g.player.right < 1) {
			return depth - 10;
		} else if ((g.player.left + g.player.right == 4)){ return -5;}
		 else return 0;
	}

	public static ArrayList<Move> GenerateMoves(Game g) {
		ArrayList<Move> moves = new ArrayList<>();
		if (g.computer.left != 0 && g.player.left != 0) {
			moves.add(new Move(false, false));
		}
		if (g.computer.left != 0 && g.player.right != 0) {
			moves.add(new Move(false, true));
		}
		if (g.computer.right != 0 && g.player.left != 0) {
			moves.add(new Move(true, false));
		}
		if (g.computer.right != 0 && g.player.right != 0) {
			moves.add(new Move(true, true));
		}

		return moves;
	}

	public static Game ApplyMove(Move m, Game g, boolean isComputer) {
		if (isComputer) {
			if (m.cRight) {
				if (m.pRight) {
					g.computer.right += g.player.right;
				} else {
					g.computer.right += g.player.left;
				}
			} else {
				if (m.pRight) {
					g.computer.left += g.player.right;
				} else {
					g.computer.left += g.player.left;
				}
			}
		} else {
			if (m.cRight) {
				if (m.pRight) {
					g.player.right += g.computer.right;
				} else {
					g.player.right += g.computer.left;
				}
			} else {
				if (m.pRight) {
					g.player.left += g.computer.right;
				} else {
					g.player.left += g.computer.left;
				}
			}
			if (g.player.left > 4) {
				g.player.left = 0;
			}

			if (g.player.right > 4) {
				g.player.right = 0;
			}

			if (g.computer.left > 4) {
				g.computer.left = 0;
			}

			if (g.computer.right > 4) {
				g.computer.right = 0;
			}
			return g;

		}

		return g;
	}
}


