package wit.cgd.xando.game;

public abstract class BasePlayer {
	public boolean human;
	public int mySymbol, opponentSymbol;
	public String name;
	public Board board;
	public int skill;

	
	public BasePlayer(Board board, int symbol) {
		this.board = board;
		setSymbol(symbol);
		human = false;
	}
	
	public void setSymbol(int symbol) {
		mySymbol = symbol;
		opponentSymbol = (symbol == board.X) ? board.O : board.X;
	}
	
	public abstract int move ();
	
}
