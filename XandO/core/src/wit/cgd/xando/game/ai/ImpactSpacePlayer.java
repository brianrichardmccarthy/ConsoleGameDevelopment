package wit.cgd.xando.game.ai;

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
        else if (board.cells[0][0] == board.EMPTY) return (0*3+0);
        else if (board.cells[0][2] == board.EMPTY) return (0*3+2);
        else if (board.cells[2][0] == board.EMPTY) return (2*3+0);
        else if (board.cells[2][2] == board.EMPTY) return (2*3+2);

        	 for (int num : new int[]{1, 3, 5, 7}) 
                 if (board.cells[num /3][num %3] == board.EMPTY) return num;
        
        return -1;
    }

}
