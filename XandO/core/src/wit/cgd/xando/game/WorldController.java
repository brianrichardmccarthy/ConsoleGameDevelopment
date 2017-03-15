package wit.cgd.xando.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;

import wit.cgd.xando.game.ai.FirstSpacePlayer;
import wit.cgd.xando.screens.MenuScreen;

public class WorldController extends InputAdapter {

    @SuppressWarnings("unused")
    private static final String TAG = WorldRenderer.class.getName();

    public float viewportWidth;
    public int width, height;
    public Board board;
    float timeLeftGameOverDelay;
    private Game game;

    public WorldController(Game game) {
        this.game = game;
        init();
    }

    private void backToMenu() {

        // switch to menu screen
        game.setScreen(new MenuScreen(game));
    }

    private void init() {

        Gdx.input.setInputProcessor(this);
        board = new Board();
        board.firstPlayer = new HumanPlayer(board, board.X);
        board.secondPlayer = new FirstSpacePlayer(board, board.O);
        board.start();

        timeLeftGameOverDelay = 2;
    }

    public void update(float deltaTime) {

        if (board.gameState == Board.GameState.PLAYING) {
            board.move();
        }

        if (board.gameState != Board.GameState.PLAYING) {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0) backToMenu();
        }
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) backToMenu();

        if (keycode == Keys.R) board.start();

        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        System.out.println("Touch down");
        if (board.gameState == Board.GameState.PLAYING && board.currentPlayer.human) {
            // convert to cell position

            int row = 4 * (height - screenY) / height;
            int col = (int) (viewportWidth * (screenX - 0.5 * width) / width) + 1;

            if (row >= 0 && row < 3 && col >= 0 && col < 3) {
                board.move(row, col);
            }
        }

        return true;

    }

}
