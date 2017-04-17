package wit.cgd.numericalxando.game.ai;

import wit.cgd.numericalxando.game.BasePlayer;
import wit.cgd.numericalxando.game.Board;


public class RandomImpactSpacePlayer extends BasePlayer {

    public RandomImpactSpacePlayer(Board board, int symbol) {
        super(board, symbol);
        name = "RandomImpactSpacePlayer";
    }

    @Override
    public int move() {

        for (int num : new int[]{4, 0, 2, 6, 8, 1, 3, 5, 7}) 
            if (board.cells[num /3][num %3] == board.EMPTY) return num;
            
        return -1;
    }

}
