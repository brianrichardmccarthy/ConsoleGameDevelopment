package wit.cgd.xando.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import wit.cgd.xando.game.util.Constants;

public class Board {

    public static final String TAG = Board.class.getName();

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

    public TextureRegion board;
    public TextureRegion x;
    public TextureRegion o;
    
    public Board() {
        init();
    }

    private void init() {
        start();
    }

    public void start() {

        for (int x = 0; x < cells.length; x++)
            for (int y = 0; y < cells[x].length; y++)
                // cells[x][y] = EMPTY;
                if ((x*3+y)%2 == 0 ) cells[x][y] = O;
                else cells[x][y] = X;

        currentPlayer = firstPlayer;
        gameState = GameState.PLAYING;
        board = Assets.instance.board.region;
        o = Assets.instance.O.region;
        x = Assets.instance.X.region;
    }

    public boolean move() {
        return move(-1, -1);
    }

    public boolean move(int row, int col) {
        if (currentPlayer instanceof HumanPlayer) {
            if (row > cells.length || col > cells[0].length || row < 0 || col < 0 || cells[row][col] != EMPTY)
                return false;
        } else {
            int cell = currentPlayer.move();
            row = cell / 3;
            col = cell % 3;

        }
        if (hasWon(currentPlayer.mySymbol, row, col))
            gameState = (currentPlayer.mySymbol == X) ? GameState.X_WON : GameState.O_WON;

        if (gameState == GameState.PLAYING && isDraw())
            gameState = GameState.DRAW;

        if (gameState == GameState.PLAYING)
            currentPlayer = (currentPlayer instanceof HumanPlayer) ? secondPlayer : firstPlayer;

        return true;
    }

    public boolean hasWon(int symbol, int row, int col) {
        int num = 0;
        for (int y = 0; y < cells[row].length; y++) {
            if (cells[row][y] == symbol)
                num++;
        }
        if (num == 2)
            return true;

        num = 0;
        for (int x = 0; x < cells[col].length; x++) {
            if (cells[x][col] == symbol)
                num++;
        }

        if (num == 2)
            return true;
        
        int pos = row*3+col;
        
        if (pos == 0 || pos == 8 || pos == 4) {
            num = 0;
            pos = 0;
            for (int x = 4; pos <= 8; pos += x) {
                int tempCol = pos % 3;
                int tempRow = pos / 3;
                if (cells[tempRow][tempCol] == symbol)
                    num++;
            }
            
            if (num == 2)
                return true;
        }
        
        pos = row*3+col;
        
        if (pos == 2 || pos == 6 || pos == 4) {
            num = 0;
            pos = 2;
            for (int x = 2; pos <= 6; pos += x) {
                int tempCol = pos % 3;
                int tempRow = pos / 3;
                if (cells[tempRow][tempCol] == symbol)
                    num++;
            }
            
            if (num == 2)
                return true;
        }
        
        return false;

    }

    public boolean isDraw() {
        for (int x = 0; x < cells.length; x++) {
            for (int y = 0; y < cells[0].length; y++) {
                if (cells[x][y] == EMPTY)
                    return false;
            }
        }
        return true;
    }
    
    public void render(SpriteBatch batch) {
        batch.draw(board.getTexture(), -Constants.VIEWPORT_WIDTH/2.25f, -Constants.VIEWPORT_HEIGHT/2, 
            0f,0f ,1, 1, 4.5f, 5, 0, board.getRegionX(), board.getRegionY(), board.getRegionWidth(), board.getRegionHeight(), false, false);
        
        for (int x = 0; x < cells.length; x++)
            for (int y = 0; y < cells[x].length; y++)
                if (cells[x][y] == X) 
                    batch.draw(this.x.getTexture(), -Constants.VIEWPORT_WIDTH/2.30f+(Constants.VIEWPORT_WIDTH/3)*x, -Constants.VIEWPORT_HEIGHT/2.25f+(Constants.VIEWPORT_HEIGHT/3)*(2-y), 
                        0f,0f , .75f, .75f, 1.5f, 1.5f, 0, this.x.getRegionX(), this.x.getRegionY(), this.x.getRegionWidth(), this.x.getRegionHeight(), false, false);
                else if (cells[x][y] == O) 
                    batch.draw(this.o.getTexture(), -Constants.VIEWPORT_WIDTH/2.25f+(Constants.VIEWPORT_WIDTH/3)*x, -Constants.VIEWPORT_HEIGHT/2.25f+(Constants.VIEWPORT_HEIGHT/3)*(2-y), 
                        0f,0f , .75f, .75f, 1.5f, 1.5f, 0, this.o.getRegionX(), this.o.getRegionY(), this.o.getRegionWidth(), this.o.getRegionHeight(), false, false);
        

    }
}