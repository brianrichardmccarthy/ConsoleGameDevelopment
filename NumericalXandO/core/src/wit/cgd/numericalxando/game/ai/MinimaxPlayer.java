/**
 * 
 * @file        MinimaxPlayer.java
 * @author      Brian McCarthy, 20063914
 * @assignment  Numerical X and O
 * @brief       Decides the move using the minimax algorithm
 * @notes       No known BUGS or ISSUES.
 *
 */
package wit.cgd.numericalxando.game.ai;

import java.util.Random;

import wit.cgd.numericalxando.game.BasePlayer;
import wit.cgd.numericalxando.game.Board;

public class MinimaxPlayer extends BasePlayer {

    private Random randomGenerator;

    public MinimaxPlayer(Board board, int symbol) {
        super(board, symbol);
        name = "MinimaxPlayer";

        skill = 3;  // skill is measure of search depth

        randomGenerator = new Random();
    }

    @Override
    public int move() {

        return (int) minimax(mySymbol, opponentSymbol, 0);
    }

    private float minimax(int p_mySymbol, int p_opponentSymbol, int depth) {

        final float WIN_SCORE = 100;
        final float DRAW_SCORE = 0;

        float score;
        float maxScore = -10000;
        int maxPos = -1;

        // for each board position
        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 3; ++c) {
                for (int x = 0; x < myNumbers.size; x++) {

                    // skip over used positions
                    if (board.cells[r][c] != board.EMPTY) continue;

                    // place move 
                    int tempNumber = 0;
                    if (mySymbol == p_mySymbol) tempNumber = myNumbers.removeIndex(x);
                    else tempNumber = oppentNumbers.removeIndex(x);
                    board.cells[r][c] = tempNumber;

                    // evaluate board (recursively)
                    if (board.hasWon(r, c)) {
                        score = WIN_SCORE;
                    } else if (board.isDraw()) {
                        score = DRAW_SCORE;
                    } else {
                        if (depth < skill) {
                            score = -minimax(p_opponentSymbol, p_mySymbol, depth + 1);
                        } else {
                            score = 0;
                        }
                    }

                    // update ranking
                    if (Math.abs(score - maxScore) < 1.0E-5 && randomGenerator.nextDouble() < 0.1) {
                        maxScore = score;
                        maxPos = 3 * r + c;
                        if ( (mySymbol == board.X && tempNumber % 2 == 1)) choice = tempNumber;
                        else if ( (mySymbol == board.O && tempNumber % 2 == 0)) choice = tempNumber;
                    } else if (score > maxScore) {    // clear 
                        maxScore = score;
                        maxPos = 3 * r + c;
                        if ( (mySymbol == board.X && tempNumber % 2 == 1)) choice = tempNumber;
                        else if ( (mySymbol == board.O && tempNumber % 2 == 0)) choice = tempNumber;
                    }

                    // undo move 
                    board.cells[r][c] = board.EMPTY;
                    if (mySymbol == p_mySymbol) myNumbers.add(tempNumber);
                    else oppentNumbers.add(tempNumber);
                }
            }
        }

        // on uppermost call return move not score
        return (depth == 0 ? maxPos : maxScore);

    };

}
