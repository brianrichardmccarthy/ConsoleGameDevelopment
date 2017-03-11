package wit.cgd.xando;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;

import wit.cgd.xando.game.Assets;
import wit.cgd.xando.game.WorldController;
import wit.cgd.xando.game.WorldRenderer;

public class XandOMain extends ApplicationAdapter {

    private WorldController worldController;
    private WorldRenderer worldRenderer;

    @Override
    public void create() {

        Assets.instance.init(new AssetManager());
        worldController = new WorldController();
        worldRenderer = new WorldRenderer(worldController);
    }

    @Override
    public void render() {

        // Update game world by the time that has passed since last rendered frame.
        worldController.update(Gdx.graphics.getDeltaTime());

        // Sets the clear screen color to: Cornflower Blue
        Gdx.gl.glClearColor(0x00 / 255.0f, 0xff / 255.0f, 0x00 / 255.0f, 0xff / 255.0f);

        // Clears the screen
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        worldRenderer.render();
    }

    @Override
    public void dispose() {

    }
}
