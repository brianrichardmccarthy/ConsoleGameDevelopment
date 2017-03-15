package wit.cgd.xando.game;

import wit.cgd.xando.game.util.Constants;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Board {

	@SuppressWarnings("unused")
	private static final String TAG = WorldRenderer.class.getName();

	public static enum GameState {
		PLAYING, DRAW, X_WON, O_WON
	}

	public GameState gameState;

	public final int EMPTY = 0;
	public final int X = 1;
	public final int O = 2;
	public int[][] cells = new int[3][3];

	public BasePlayer firstPlayer, secondPlayer;
	public BasePlayer currentPlayer;

	public Board() {
		init();
	}

	public void init() {
		start();
	}

	public void start() {
		for (int r = 0; r < 3; r++)
			for (int c = 0; c < 3; c++)
				cells[r][c] = EMPTY;

		gameState = GameState.PLAYING;
		currentPlayer = firstPlayer;
	}

	public boolean move() {
		return move(-1, -1);
	}

	public boolean move(int row, int col) {

		if (currentPlayer.human) {
			if (row < 0 || col < 0 || row > 2 || col > 2
					|| cells[row][col] != EMPTY)
				return false;
		} else { // computer player
			int pos = currentPlayer.move();
			col = pos % 3;
			row = pos / 3;
		}

		System.out.println(" " + currentPlayer.human + " " + row + " " + col);
		// store move
		cells[row][col] = currentPlayer.mySymbol;

		System.out.print("Board:");
		for (int r=0; r<3; r++)
			for (int c=0; c<3; c++)
				System.out.print(" " + cells[r][c]);
		System.out.println();
		if (hasWon(currentPlayer.mySymbol, row, col)) {
			gameState = currentPlayer.mySymbol == X ? GameState.X_WON
					: GameState.O_WON;
		} else if (isDraw()) {
			gameState = GameState.DRAW;
		}

		// switch player
		if (gameState == GameState.PLAYING) {
			currentPlayer = (currentPlayer == firstPlayer ? secondPlayer
					: firstPlayer);
		}

		return true;
	}

	public boolean isDraw() {
		for (int r = 0; r < 3; ++r) {
			for (int c = 0; c < 3; ++c) {
				if (cells[r][c] == EMPTY) {
					return false; // an empty seed found, not a draw, exit
				}
			}
		}
		return true; // no empty cell, it's a draw
	}

	public boolean hasWon(int symbol, int row, int col) {
		return (
			// 3-in-the-row
			cells[row][0] == symbol && cells[row][1] == symbol && cells[row][2] == symbol
			||  // 3-in-the-column
			cells[0][col] == symbol && cells[1][col] == symbol && cells[2][col] == symbol
			||  // 3-in-the-diagonal
			row == col && cells[0][0] == symbol && cells[1][1] == symbol && cells[2][2] == symbol 
			|| // 3-in-the-opposite-diagonal
			row + col == 2 && cells[0][2] == symbol && cells[1][1] == symbol && cells[2][0] == symbol
		);
	}

	public void render(SpriteBatch batch) {
		TextureRegion region = Assets.instance.board.region;
		batch.draw(region.getTexture(), -2,
				-Constants.VIEWPORT_HEIGHT / 2 + 0.1f, 0, 0, 4, 4, 1, 1, 0,
				region.getRegionX(), region.getRegionY(),
				region.getRegionWidth(), region.getRegionHeight(), false, false);

		for (int row = 0; row < 3; row++)
			for (int col = 0; col < 3; col++) {
				if (cells[row][col] == EMPTY) continue;
				region = cells[row][col] == X ? Assets.instance.x.region
						: Assets.instance.o.region;
				batch.draw(region.getTexture(), col*1.4f-1.9f,
						row*1.4f-2.3f, 0, 0, 1, 1, 1, 1, 0,
						region.getRegionX(), region.getRegionY(),
						region.getRegionWidth(), region.getRegionHeight(),
						false, false);
			}

	}

}
