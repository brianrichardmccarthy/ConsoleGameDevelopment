package wit.cgd.numericalxando.game;

import java.util.Stack;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import wit.cgd.numericalxando.game.ai.MinimaxPlayer;
import wit.cgd.numericalxando.game.util.AudioManager;
import wit.cgd.numericalxando.game.util.Constants;

public class Board {

    private class Previous {

        public int row;
        public int col;
        public int number;
        public int playerSymbol;

        public Previous(int row, int col, int number, int playerSymbol) {
            this.row = row;
            this.col = col;
            this.number = number;
            this.playerSymbol = playerSymbol;
        }

    }

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
    private Stack<Previous> previous;

    public Vector3 suggested;

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
        previous = new Stack<Previous>();
    }

    public boolean move() {

        return move(-1, -1, -1);
    }

    public boolean move(int row, int col, int number) {

        if (currentPlayer.human) {
            AudioManager.instance.play(Assets.instance.sounds.first);
            if (row < 0 || col < 0 || row > 2 || col > 2 || cells[row][col] != EMPTY) return false;
        } else { // computer player
            AudioManager.instance.play(Assets.instance.sounds.second);
            int pos = currentPlayer.move();
            col = pos % 3;
            row = pos / 3;
            number = currentPlayer.choice;
        }

        // store move
        cells[row][col] = number;
        currentPlayer.remove(number);
        previous.push(new Previous(row, col, number, currentPlayer.mySymbol));
        Gdx.app.debug(TAG, "Current player <" + currentPlayer.mySymbol + "> Number <" + number + ">");

        if (hasWon(row, col)) {
            gameState = currentPlayer.mySymbol == X ? GameState.X_WON : GameState.O_WON;
            AudioManager.instance.play(Assets.instance.sounds.win);
        } else if (isDraw()) {
            AudioManager.instance.play(Assets.instance.sounds.draw);
            gameState = GameState.DRAW;
        }

        // switch player
        if (gameState == GameState.PLAYING) {
            currentPlayer = (currentPlayer == firstPlayer) ? secondPlayer : firstPlayer;
        }

        return true;
    }

    public boolean isDraw() {

        for (int r = 0; r < 3; ++r) {
            for (int c = 0; c < 3; ++c) {
                if (cells[r][c] == EMPTY) { return false; // an empty seed found, not a draw, exit
                }
            }
        }
        return true; // no empty cell, it's a draw
    }

    public boolean hasWon(int row, int col) {

        return (
        // 3-in-the-row
        ( (cells[row][0] + cells[row][1] + cells[row][2] == 15) && (cells[row][0] != EMPTY && cells[row][1] != EMPTY && cells[row][2] != EMPTY)) ||  // 3-in-the-column
                (cells[0][col] + cells[1][col] + cells[2][col] == 15 && (cells[0][col] != EMPTY && cells[1][col] != EMPTY && cells[2][col] != EMPTY)) ||  // 3-in-the-diagonal
                (row == col && cells[0][0] + cells[1][1] + cells[2][2] == 15 && (cells[0][0] != EMPTY && cells[1][1] != EMPTY && cells[2][2] != EMPTY)) || // 3-in-the-opposite-diagonal
                (row + col == 2 && cells[0][2] + cells[1][1] + cells[2][0] == 15 && (cells[0][2] != EMPTY && cells[1][1] != EMPTY && cells[2][0] != EMPTY)));
    }

    public void undo() {

        if (!previous.isEmpty()) {
            Previous temp = previous.pop();
            cells[temp.row][temp.col] = EMPTY;
            if (temp.playerSymbol == firstPlayer.mySymbol) firstPlayer.myNumbers.add(temp.number);
            else secondPlayer.myNumbers.add(temp.number);
        }
    }

    public void help() {

        MinimaxPlayer tempPlayer = new MinimaxPlayer(this, currentPlayer.mySymbol);
        tempPlayer.myNumbers.clear();
        tempPlayer.oppentNumbers.clear();
        tempPlayer.myNumbers = new Array<Integer>(currentPlayer.myNumbers);
        tempPlayer.oppentNumbers = new Array<Integer>(currentPlayer.oppentNumbers);
        int p = tempPlayer.move();
        suggested = new Vector3(p / 3, p % 3, tempPlayer.choice);
    }

    public void render(SpriteBatch batch) {

        TextureRegion region = Assets.instance.board.region;
        batch.draw(region.getTexture(), -2, -Constants.VIEWPORT_HEIGHT / 2 + 0.1f, 0, 0, 4, 4, 1, 1, 0,
            region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), false, false);

        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++) {
                if (cells[row][col] == EMPTY) continue;
                if (Assets.instance.numbers.get(cells[row][col]) != null) batch.draw(Assets.instance.numbers.get(
                    cells[row][col]).region.getTexture(), col * 1.4f - 1.9f, row * 1.4f - 2.3f, 0, 0, 1, 1, 1, 1, 0,
                    Assets.instance.numbers.get(cells[row][col]).region.getRegionX(), Assets.instance.numbers.get(
                        cells[row][col]).region.getRegionY(), Assets.instance.numbers.get(
                            cells[row][col]).region.getRegionWidth(), Assets.instance.numbers.get(
                                cells[row][col]).region.getRegionHeight(), false, false);
            }

        if (firstPlayer.valid(1))
            // one
            batch.draw(Assets.instance.numbers.get(1).region.getTexture(), (-1) * 1.4f - 2.7f, 1 * 1.4f - .2f, 0, 0, 1,
                1, 1, 1, 0, Assets.instance.numbers.get(1).region.getRegionX(), Assets.instance.numbers.get(
                    1).region.getRegionY(), Assets.instance.numbers.get(1).region.getRegionWidth(),
                Assets.instance.numbers.get(1).region.getRegionHeight(), false, false);

        if (firstPlayer.valid(3))
            // three
            batch.draw(Assets.instance.numbers.get(3).region.getTexture(), (-1) * 1.4f - 2.7f, 1 * 1.4f - 1.5f, 0, 0, 1,
                1, 1, 1, 0, Assets.instance.numbers.get(3).region.getRegionX(), Assets.instance.numbers.get(
                    3).region.getRegionY(), Assets.instance.numbers.get(3).region.getRegionWidth(),
                Assets.instance.numbers.get(3).region.getRegionHeight(), false, false);

        if (firstPlayer.valid(5))
            // five
            batch.draw(Assets.instance.numbers.get(5).region.getTexture(), (-1) * 1.4f - 2.7f, 1 * 1.4f - 2.7f, 0, 0, 1,
                1, 1, 1, 0, Assets.instance.numbers.get(5).region.getRegionX(), Assets.instance.numbers.get(
                    5).region.getRegionY(), Assets.instance.numbers.get(5).region.getRegionWidth(),
                Assets.instance.numbers.get(5).region.getRegionHeight(), false, false);

        if (firstPlayer.valid(7))
            // seven
            batch.draw(Assets.instance.numbers.get(7).region.getTexture(), (-1) * 1.4f - 1.7f, 1 * 1.4f - 1f, 0, 0, 1,
                1, 1, 1, 0, Assets.instance.numbers.get(7).region.getRegionX(), Assets.instance.numbers.get(
                    7).region.getRegionY(), Assets.instance.numbers.get(7).region.getRegionWidth(),
                Assets.instance.numbers.get(7).region.getRegionHeight(), false, false);

        if (firstPlayer.valid(9))
            // nine
            batch.draw(Assets.instance.numbers.get(9).region.getTexture(), (-1) * 1.4f - 1.7f, 1 * 1.4f - 2.2f, 0, 0, 1,
                1, 1, 1, 0, Assets.instance.numbers.get(9).region.getRegionX(), Assets.instance.numbers.get(
                    9).region.getRegionY(), Assets.instance.numbers.get(9).region.getRegionWidth(),
                Assets.instance.numbers.get(9).region.getRegionHeight(), false, false);

        if (secondPlayer.valid(2))
            // two
            batch.draw(Assets.instance.numbers.get(2).region.getTexture(), (3) * 1.4f - 2f, 1 * 1.4f - .3f, 0, 0, 1, 1,
                1, 1, 0, Assets.instance.numbers.get(2).region.getRegionX(), Assets.instance.numbers.get(
                    2).region.getRegionY(), Assets.instance.numbers.get(2).region.getRegionWidth(),
                Assets.instance.numbers.get(2).region.getRegionHeight(), false, false);

        if (secondPlayer.valid(4))
            // four
            batch.draw(Assets.instance.numbers.get(4).region.getTexture(), (3) * 1.4f - 1.2f, 1 * 1.4f - 1.5f, 0, 0, 1,
                1, 1, 1, 0, Assets.instance.numbers.get(4).region.getRegionX(), Assets.instance.numbers.get(
                    4).region.getRegionY(), Assets.instance.numbers.get(4).region.getRegionWidth(),
                Assets.instance.numbers.get(4).region.getRegionHeight(), false, false);

        if (secondPlayer.valid(6))
            // six
            batch.draw(Assets.instance.numbers.get(6).region.getTexture(), (3) * 1.4f - 2f, 1 * 1.4f - 2.6f, 0, 0, 1, 1,
                1, 1, 0, Assets.instance.numbers.get(6).region.getRegionX(), Assets.instance.numbers.get(
                    6).region.getRegionY(), Assets.instance.numbers.get(6).region.getRegionWidth(),
                Assets.instance.numbers.get(6).region.getRegionHeight(), false, false);

        if (secondPlayer.valid(8))
            // eight
            batch.draw(Assets.instance.numbers.get(8).region.getTexture(), (3) * 1.4f - 1.2f, 1 * 1.4f - 3.8f, 0, 0, 1,
                1, 1, 1, 0, Assets.instance.numbers.get(8).region.getRegionX(), Assets.instance.numbers.get(
                    8).region.getRegionY(), Assets.instance.numbers.get(8).region.getRegionWidth(),
                Assets.instance.numbers.get(8).region.getRegionHeight(), false, false);
    }

}
