package wit.cgd.numericalxando.game.ai;

import java.util.Random;

import wit.cgd.numericalxando.game.BasePlayer;
import wit.cgd.numericalxando.game.Board;

public class RandomSpacePlayer extends BasePlayer {

    public RandomSpacePlayer(Board board, int symbol) {
        super(board, symbol);
        name = "RandomImpactSpacePlayer";
    }

    @Override
    public int move() {
        Random rand = new Random();
        
        choice = myNumbers.first();
        
        while (true) {
            int space = rand.nextInt(9);
            if (board.cells[space / 3][space % 3] == board.EMPTY) return space;
        }
        
    }

}
