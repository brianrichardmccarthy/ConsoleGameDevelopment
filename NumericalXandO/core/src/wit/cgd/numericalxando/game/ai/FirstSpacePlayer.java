package wit.cgd.numericalxando.game.ai;

import sun.awt.image.PNGImageDecoder.Chromaticities;
import wit.cgd.numericalxando.game.BasePlayer;
import wit.cgd.numericalxando.game.Board;
import wit.cgd.numericalxando.game.WorldRenderer;

public class FirstSpacePlayer extends BasePlayer {

	@SuppressWarnings("unused")
	private static final String	TAG	= WorldRenderer.class.getName(); 

	public FirstSpacePlayer(Board board, int symbol) {
		super(board, symbol);
		name = "FirstSpacePlayer";
	}

	@Override
	public int move() {
		for (int r=2; r>=0; --r)
			for (int c=0; c<3; ++c) 
				if (board.cells[r][c]==board.EMPTY) {
				    choice = myNumbers.first();
				    return r*3+c;
				}
		
		return -1;
		
	}

}
