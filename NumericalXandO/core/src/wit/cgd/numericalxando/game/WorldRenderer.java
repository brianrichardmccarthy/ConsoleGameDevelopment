/**
 * 
 * @file        WorldRenderer.java
 * @author      Brian McCarthy, 20063914
 * @assignment  Numerical X and O
 * @brief       Renders text and the game
 * @notes       No known BUGS or ISSUES.
 *
 */
package wit.cgd.numericalxando.game;

import wit.cgd.numericalxando.game.WorldController;
import wit.cgd.numericalxando.game.Board.GameState;
import wit.cgd.numericalxando.game.util.Constants;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;

public class WorldRenderer implements Disposable {

    @SuppressWarnings("unused")
    private static final String TAG = WorldRenderer.class.getName();

    public OrthographicCamera camera;
    public OrthographicCamera cameraGUI;

    private SpriteBatch batch;
    private WorldController worldController;

    public WorldRenderer(WorldController worldController) {
        this.worldController = worldController;
        init();
    }

    private void init() {

        batch = new SpriteBatch();
        camera = new OrthographicCamera(Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT);
        camera.position.set(0, 0, 0);
        camera.update();
        cameraGUI = new OrthographicCamera(Constants.VIEWPORT_GUI_WIDTH, Constants.VIEWPORT_GUI_HEIGHT);
        cameraGUI.position.set(0, 0, 0);
        cameraGUI.setToOrtho(true); // flip y-axis
        cameraGUI.update();
    }

    public void resize(int width, int height) {

        camera.viewportWidth = (Constants.VIEWPORT_HEIGHT / (float) height) * (float) width;
        camera.update();
        worldController.viewportWidth = camera.viewportWidth;
        worldController.width = width;
        worldController.height = height;
        cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
        cameraGUI.viewportWidth = (Constants.VIEWPORT_GUI_HEIGHT / (float) height) * (float) width;
        cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
        cameraGUI.update();
    }

    public void render() {

        batch.setProjectionMatrix(camera.combined);
        batch.begin();
        worldController.board.render(batch);

        if (worldController.dragging) {

            float x = worldController.dragX;
            x = (float) (worldController.viewportWidth * (x - 0.5 * worldController.width) / worldController.width);

            float y = worldController.dragY;
            y = (float) (4.0 * (worldController.height - y) / worldController.height - 2.5);

            batch.draw(worldController.dragRegion.getTexture(), x, y, 0, 0, 1, 1, 1, 1, 0,
                worldController.dragRegion.getRegionX(), worldController.dragRegion.getRegionY(),
                worldController.dragRegion.getRegionWidth(), worldController.dragRegion.getRegionHeight(), false,
                false);
        }

        batch.end();

        // GUI rendering
        if (worldController.board.gameState != Board.GameState.PLAYING) {
            batch.setProjectionMatrix(cameraGUI.combined);
            batch.begin();
            float x = cameraGUI.viewportWidth / 2;
            float y = cameraGUI.viewportHeight / 2;
            BitmapFont fontGameOver = Assets.instance.fonts.defaultBig;
            fontGameOver.setColor(1, 0.75f, 0.25f, 1);
            String message = "X Won";
            if (worldController.board.gameState == GameState.O_WON) message = "O Won";
            else if (worldController.board.gameState == GameState.DRAW) message = "Draw";
            fontGameOver.draw(batch, message, x, y, 0, Align.center, true);
            fontGameOver.setColor(1, 1, 1, 1);
            batch.end();
        }

    }

    @Override
    public void dispose() {

        batch.dispose();
    }
}
