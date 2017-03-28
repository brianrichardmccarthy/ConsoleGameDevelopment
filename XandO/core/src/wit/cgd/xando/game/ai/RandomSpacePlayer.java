package wit.cgd.xando.game.ai;

import java.util.Random;

import wit.cgd.xando.game.BasePlayer;
import wit.cgd.xando.game.Board;

public class RandomSpacePlayer extends BasePlayer {

    public RandomSpacePlayer(Board board, int symbol) {
        super(board, symbol);
        name = "RandomImpactSpacePlayer";
    }

    @Override
    public int move() {
        Random rand = new Random();
        
        while (true) {
            int space = rand.nextInt(9);
            if (board.cells[space / 3][space % 3] == board.EMPTY) return space;
        }
        
    }

}
