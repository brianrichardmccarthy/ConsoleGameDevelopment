package wit.cgd.xando.game;


public class FirstSpacePlayer extends BasePlayer {

    public FirstSpacePlayer(Board board, int symbol) {
        super(board, symbol);
        super.human = false;
    }

    @Override
    public int move() {

        
        for (int x = 0; x < board.cells.length; x++) {
            for (int y = 0; y < board.cells[x].length; y++) {
                if (board.cells[x][y] == board.EMPTY) return x*3+y;
            }
        }
        
        return -1;
    }

}
