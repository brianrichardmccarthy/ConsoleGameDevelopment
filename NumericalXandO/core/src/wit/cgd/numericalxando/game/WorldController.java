package wit.cgd.numericalxando.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import wit.cgd.numericalxando.game.ai.CheckAndImpactPlayer;
import wit.cgd.numericalxando.game.ai.FirstSpacePlayer;
import wit.cgd.numericalxando.game.ai.ImpactSpacePlayer;
import wit.cgd.numericalxando.game.ai.MinimaxPlayer;
import wit.cgd.numericalxando.game.ai.RandomImpactSpacePlayer;
import wit.cgd.numericalxando.game.ai.RandomSpacePlayer;
import wit.cgd.numericalxando.game.util.GamePreferences;
import wit.cgd.numericalxando.game.util.GameStats;
import wit.cgd.numericalxando.screens.MenuScreen;

public class WorldController extends InputAdapter {

    private static final String TAG = WorldRenderer.class.getName();

    public float viewportWidth;
    public int width, height;
    public Board board;
    private Game game;
    private boolean updateScore;
    boolean dragging = false;
    int dragX, dragY;
    TextureRegion dragRegion;

    float timeLeftGameOverDelay;
    final float TIME_LEFT_GAME_OVER_DELAY = 3;
    public int gameCount = 0;
    public int win = 0, draw = 0, loss = 0;

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
         
         if (GamePreferences.instance.firstPlayerHuman) board.firstPlayer = new HumanPlayer(board, board.X);
         else if (GamePreferences.instance.firstPlayerSkill <= 1.0f) board.firstPlayer = new FirstSpacePlayer(board, board.X);
         else if (GamePreferences.instance.firstPlayerSkill <= 2.0f) board.firstPlayer = new RandomSpacePlayer(board, board.X);
         else if (GamePreferences.instance.firstPlayerSkill <= 3.0f) board.firstPlayer = new ImpactSpacePlayer(board, board.X);
         else if (GamePreferences.instance.firstPlayerSkill <= 5.0f) board.firstPlayer = new RandomImpactSpacePlayer(board, board.X);
         else if (GamePreferences.instance.firstPlayerSkill <= 7.0f) board.firstPlayer = new CheckAndImpactPlayer(board, board.X);
         else board.firstPlayer = new MinimaxPlayer(board, board.X);
         
         if (GamePreferences.instance.secondPlayerHuman) board.secondPlayer = new HumanPlayer(board, board.O);
         else if (GamePreferences.instance.secondPlayerSkill <= 1.0f) board.secondPlayer = new FirstSpacePlayer(board, board.O);
         else if (GamePreferences.instance.secondPlayerSkill <= 2.0f) board.secondPlayer = new RandomSpacePlayer(board, board.O);
         else if (GamePreferences.instance.secondPlayerSkill <= 3.0f) board.secondPlayer = new ImpactSpacePlayer(board, board.O);
         else if (GamePreferences.instance.secondPlayerSkill <= 5.0f) board.secondPlayer = new RandomImpactSpacePlayer(board, board.O);
         else if (GamePreferences.instance.secondPlayerSkill <= 7.0f) board.secondPlayer = new CheckAndImpactPlayer(board, board.O);
         else board.secondPlayer = new MinimaxPlayer(board, board.O);
         
         updateScore = true;
         timeLeftGameOverDelay = 2;
         board.start();
    }

     public void update(float deltaTime) {
    
        if (board.gameState == Board.GameState.PLAYING) {
            board.move();
        }
    
        if (board.gameState != Board.GameState.PLAYING) {
            if (updateScore) {
                if (board.gameState == Board.GameState.X_WON) {
                    GameStats.instance.win();
                } else if (board.gameState == Board.GameState.O_WON) {
                    GameStats.instance.lose();
                } else {
                    GameStats.instance.draw();
                }
                updateScore = false;
            }
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

        if (board.gameState == Board.GameState.PLAYING && board.currentPlayer.human) {

            // convert to cell position
            int row = 4 * (height - screenY) / height;
            int col = (int) (viewportWidth * (screenX - 0.5 * width) / width) + 1;

            // board move - just place piece and return
            if (row >= 0 && row < 3 && col >= 0 && col < 3) {
                board.move(row, col, board.currentPlayer.myNumbers.get(0));
                dragRegion = null;
                return true;
            }

            dragX = screenX;
            dragY = screenY;

            if (row == 3 && col == -2 && board.currentPlayer == board.firstPlayer) {
                dragging = true;
                dragRegion = Assets.instance.numbers.get(1).region;
                return true;
            }
            
            if (row == 3 && col == 3 && board.currentPlayer == board.secondPlayer) {
                dragging = true;
                dragRegion = Assets.instance.numbers.get(2).region;
                return true;
            }
            
            if (row == 2 && col == -2 && board.currentPlayer == board.firstPlayer) {
                dragging = true;
                dragRegion = Assets.instance.numbers.get(3).region;
                return true;
            }
            
            if (row == 2 && col == 4 && board.currentPlayer == board.secondPlayer) {
                dragging = true;
                dragRegion = Assets.instance.numbers.get(4).region;
                return true;
            }
            
            if (row == 1 && col == -2 && board.currentPlayer == board.firstPlayer) {
                dragging = true;
                dragRegion = Assets.instance.numbers.get(5).region;
                return true;
            }
            
            if (row == 1 && col == 3 && board.currentPlayer == board.secondPlayer) {
                dragging = true;
                dragRegion = Assets.instance.numbers.get(6).region;
                return true;
            }
            
            if (row == 2 && col == -1 && board.currentPlayer == board.firstPlayer) {
                dragging = true;
                dragRegion = Assets.instance.numbers.get(7).region;
                return true;
            }
            
            if (row == 0 && col == 4 && board.currentPlayer == board.secondPlayer) {
                dragging = true;
                dragRegion = Assets.instance.numbers.get(8).region;
                return true;
            }
            
            if (row == 1 && col == -1 && board.currentPlayer == board.firstPlayer) {
                dragging = true;
                dragRegion = Assets.instance.numbers.get(9).region;
                return true;
            }
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        dragX = screenX;
        dragY = screenY;
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        dragging = false;

        if (dragRegion == null) return true;
        
        // convert to cell position
        int row = 4 * (height - screenY) / height;
        int col = (int) (viewportWidth * (screenX - 0.5 * width) / width) + 1;

        // if a valid board cell then place piece
        if (row >= 0 && row < 3 && col >= 0 && col < 3 && dragRegion != null) {
            int x = (Integer.parseInt(dragRegion.toString()));
            board.move(row, col, x);
            dragRegion = null;
            return true;
        }

        return true;
    }

}
