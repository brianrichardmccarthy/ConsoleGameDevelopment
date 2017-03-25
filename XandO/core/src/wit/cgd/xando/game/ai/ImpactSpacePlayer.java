package wit.cgd.xando.game.ai;

import java.util.Random;

import wit.cgd.xando.game.BasePlayer;
import wit.cgd.xando.game.Board;


public class ImpactSpacePlayer extends BasePlayer {

    public ImpactSpacePlayer(Board board, int symbol) {
        super(board, symbol);
        name = "ImpactSpacePlayer";
    }

    @Override
    public int move() {

        if (board.cells[1][1] == board.EMPTY) return (1*3+1);
        else if (board.cells[0][0] == board.EMPTY) return (0*0+1);
        else if (board.cells[0][2] == board.EMPTY) return (0*0+1);
        else if (board.cells[2][0] == board.EMPTY) return (0*0+1);
        else if (board.cells[2][2] == board.EMPTY) return (0*0+1);
        
        Random rand = new Random();
        while (true) {
            int space = rand.nextInt(9);
            int row = space / 3;
            int col = space % 3;
            if (board.cells[row][col] == board.EMPTY) return space;
        }
        
    }

}
