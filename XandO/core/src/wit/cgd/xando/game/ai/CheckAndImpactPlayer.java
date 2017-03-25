package wit.cgd.xando.game.ai;

import wit.cgd.xando.game.BasePlayer;
import wit.cgd.xando.game.Board;


public class CheckAndImpactPlayer extends BasePlayer {

    public CheckAndImpactPlayer(Board board, int symbol) {
        super(board, symbol);
        name = "CheckAndImpactPlayer";
    }

    @Override
    public int move() {

        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.cells[row][col] == board.EMPTY) {
                    board.cells[row][col] = mySymbol;
                    if (board.hasWon(mySymbol, row, col)) {
                        board.cells[row][col] = board.EMPTY;
                        return (row*3+col);
                    }
                }
            }
        }
        
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                if (board.cells[row][col] == board.EMPTY) {
                    board.cells[row][col] = opponentSymbol;
                    if (board.hasWon(opponentSymbol, row, col)) {
                        board.cells[row][col] = board.EMPTY;
                        return (row*3+col);
                    }
                }
            }
        }
        
        for (int num : new int[]{4, 0, 2, 6, 8, 1, 3, 5, 7}) 
            if (board.cells[num /3][num %3] == board.EMPTY) return num;
        
        return 7;
        
    }

}
