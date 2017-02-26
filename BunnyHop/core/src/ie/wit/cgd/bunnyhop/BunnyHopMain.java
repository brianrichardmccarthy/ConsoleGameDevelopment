package ie.wit.cgd.bunnyhop;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.MathUtils;

import ie.wit.cgd.bunnyhop.game.Assets;
import ie.wit.cgd.bunnyhop.game.ProceduralGeneratedLevel;
import ie.wit.cgd.bunnyhop.game.WorldController;
import ie.wit.cgd.bunnyhop.game.WorldRenderer;

public class BunnyHopMain extends ApplicationAdapter {

    private static final String TAG = BunnyHopMain.class.getName();

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    private boolean paused;

    @Override
    public void create() {

        // Set Libgdx log level to DEBUG
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        // Load assets
        Assets.instance.init(new AssetManager());

        ProceduralGeneratedLevel temp = new ProceduralGeneratedLevel();
        temp.generateLevel("C:\\Projects\\Java\\ConsoleGameDevelopment1\\BunnyHop\\android\\assets\\levels\\level-03");

        // Initialize controller and renderer
        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController);
        paused = false;
    }

    @Override
    public void render() {

        // Do not update game world when paused
        if (!paused) {
            // Update game world by the time that has passed since last rendered frame
            worldController.update(MathUtils.clamp(Gdx.graphics.getDeltaTime(), 0f, 0.05f));

            // Sets the clear screen color to: Cornflower Blue
            Gdx.gl.glClearColor(0x64 / 255.0f, 0x95 / 255.0f, 0xed / 255.0f, 0xff / 255.0f);

            // Clears the screen
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

            // Render game world to screen
            worldRenderer.render();
        }
    }

    @Override
    public void resize(int width, int height) {

        worldRenderer.resize(width, height);
    }

    @Override
    public void pause() {

        paused = true;
    }

    @Override
    public void resume() {

        Assets.instance.init(new AssetManager());
        paused = false;
    }

    @Override
    public void dispose() {

        Assets.instance.dispose();
        worldRenderer.dispose();
    }
}
