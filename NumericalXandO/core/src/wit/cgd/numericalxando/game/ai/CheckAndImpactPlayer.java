/**
 * 
 * @file        CheckAndImpactPlayer.java
 * @author      Brian McCarthy, 20063914
 * @assignment  Numerical X and O
 * @brief       Plays by deciding if the next move can win the game, place it. Else if the opponents next move can win the game block it. Else place a random piece in most valuable spot
 * @notes       No known BUGS or ISSUES.
 *
 */
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

        // Check if this player can win - if yes win the game
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < myNumbers.size; y++) {
                int row = x / 3;
                int col = x % 3;
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

        // check if opponent can win - if yes stop them
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < oppentNumbers.size; y++) {
                int row = x / 3;
                int col = x % 3;
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

        // place a random number from the available numbers
        choice = myNumbers.random();
        for (int num: new int[]{ 4, 0, 2, 6, 8, 1, 3, 5, 7 })
            if (board.cells[num / 3][num % 3] == board.EMPTY) return num;

        return -1;
    }

}
