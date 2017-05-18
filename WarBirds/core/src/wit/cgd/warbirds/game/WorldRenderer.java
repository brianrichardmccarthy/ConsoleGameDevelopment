package wit.cgd.warbirds.game;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

import wit.cgd.warbirds.game.util.Constants;

public class WorldRenderer implements Disposable {

	private static final String	TAG	= WorldRenderer.class.getName();

	public OrthographicCamera	camera;
	public OrthographicCamera	cameraGUI;
	
	private SpriteBatch			batch;
	private WorldController		worldController;

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
		
		float scale = (float)height/(float)width;
		camera.viewportHeight = scale * Constants.VIEWPORT_HEIGHT;
		camera.update();
		cameraGUI.viewportHeight = Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.viewportWidth = scale*Constants.VIEWPORT_GUI_HEIGHT;
		cameraGUI.position.set(cameraGUI.viewportWidth / 2, cameraGUI.viewportHeight / 2, 0);
		cameraGUI.update();
		
		// update level decoration
		worldController.level.levelDecoration.scale.y =  scale;
	}
	
	private void renderGuiScore(SpriteBatch batch) {
	    batch.draw(Assets.instance.enemy[0].region, -15, -15, 50, 50, 100, 100, 0.80f, -0.80f, 0);
	    Assets.instance.fonts.defaultBig.draw(batch, "" + worldController.level.killedEnemies + "/" + worldController.level.totalNumberOfEnemies, -15+100, -15+37);
	}
	
	private void renderGuiExtraLive(SpriteBatch batch) {
        float x = cameraGUI.viewportWidth - 50 - Constants.MAX_LIVES * 100;
        float y = -15;
        for (int i = 0; i < Constants.MAX_LIVES; i++) {
            if (worldController.lives <= i) batch.setColor(0.5f, 0.5f, 0.5f, 0.5f);
            batch.draw(Assets.instance.player.region, x + i * 100, y, 50, 50, 120, 100, 0.80f, -0.80f, 0);
            batch.setColor(1, 1, 1, 1);
        }
    }
	
	public void render() {
		
		// Game rendering
		worldController.cameraHelper.applyTo(camera);
		batch.setProjectionMatrix(camera.combined);
		batch.begin();
		worldController.level.render(batch);
		batch.end();
		
		// GUI + HUD rendering 
		
		batch.setProjectionMatrix(cameraGUI.combined);
		batch.begin();
		renderGuiScore(batch);
		renderGuiExtraLive(batch);
		batch.end();
	}

	@Override
	public void dispose() {
		batch.dispose();
	}
}
