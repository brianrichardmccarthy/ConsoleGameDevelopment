package wit.cgd.xando.game;

public class HumanPlayer extends BasePlayer {

	@SuppressWarnings("unused")
	private static final String	TAG	= WorldRenderer.class.getName(); 

	public HumanPlayer(Board board, int symbol) {
		super(board, symbol);
		human = true;
		name = "Electrified Meat";
	}

	@Override
	public int move() {
		// human move handled in worldController
		return 0;
	}

}
