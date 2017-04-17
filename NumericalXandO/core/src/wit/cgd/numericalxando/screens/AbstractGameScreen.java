package wit.cgd.numericalxando.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import wit.cgd.numericalxando.game.Assets;
import wit.cgd.numericalxando.game.util.Constants;

public abstract class AbstractGameScreen implements Screen {

    protected Game game;
    protected Skin skin;
    protected Skin defaultSkin;

    public AbstractGameScreen(Game game) {
        this.game = game;
        skin = new Skin(Gdx.files.internal(Constants.SKIN_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
        defaultSkin = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
    }

    @Override
    public abstract void show();

    @Override
    public abstract void render(float delta);

    @Override
    public abstract void resize(int width, int height);

    @Override
    public abstract void pause();

    @Override
    public abstract void hide();

    @Override
    public void resume() {

        Assets.instance.init(new AssetManager());
    }

    @Override
    public void dispose() {
        skin.dispose();
        defaultSkin.dispose();
        Assets.instance.dispose();
    }

}
