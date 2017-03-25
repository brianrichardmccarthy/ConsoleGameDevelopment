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
            int row = space / 3;
            int col = space % 3;
            if (board.cells[row][col] == board.EMPTY) return space;
        }
        
    }

}
