package wit.cgd.numericalxando.game.ai;

import wit.cgd.numericalxando.game.BasePlayer;
import wit.cgd.numericalxando.game.Board;

public class CheckAndImpactPlayer extends BasePlayer {

	public CheckAndImpactPlayer(Board board, int symbol) {
		super(board, symbol);
		name = "CheckAndImpactPlayer";
	}

	@Override
	public int move() {

		for (int x = 0; x < 8; x++) {
		    for (int y = 0; y < myNumbers.size; y++) {
		        int row = x/3;
		        int col = x%3;
		        if (board.cells[row][col] == board.EMPTY) {
		            board.cells[row][col] = myNumbers.get(y);
		            if (board.hasWon(row, col)) {
		                choice = myNumbers.get(y);
		                board.cells[row][col] = board.EMPTY;
		                return x;
		            }
		            board.cells[row][col] = board.EMPTY;
		        }
		    }
		}

		for (int x = 0; x < 8; x++) {
		    for (int y = 0; y < oppentNumbers.size; y++) {
		        int row = x/3;
		        int col = x%3;
		        if (board.cells[row][col] == board.EMPTY) {
		            board.cells[row][col] = oppentNumbers.get(y);
		            if (board.hasWon(row, col)) {
		                choice = myNumbers.first();
		                board.cells[row][col] = board.EMPTY;
		                return x;
		            }
		            board.cells[row][col] = board.EMPTY;
		        }
		    }
		}
		
		for (int num : new int[] { 4, 0, 2, 6, 8, 1, 3, 5, 7 })
			if (board.cells[num / 3][num % 3] == board.EMPTY) return num;

		return -1;

	}

}
