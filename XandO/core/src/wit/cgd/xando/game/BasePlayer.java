package wit.cgd.xando.game;


public abstract class BasePlayer {

    public boolean  human;
    public int      mySymbol, opponentSymbol;
    public String   name;
    public Board    board;
    
    public BasePlayer(Board board, int symbol) {
        this.board = board;
        human = false;
    }
    
    public void setSymbol(int symbol) {
        mySymbol = (symbol == board.X) ? board.X : board.O;
        opponentSymbol = (symbol == board.X) ? board.O : board.X;
    }
    
    public abstract int move ();
}
