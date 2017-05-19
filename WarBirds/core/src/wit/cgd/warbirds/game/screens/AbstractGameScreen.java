/**
 *
 * @file        AbstractGameScreen
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Abstract/ base screen class 
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game.screens;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.util.Constants;

public abstract class AbstractGameScreen implements Screen {

	protected Game	game;
	protected Skin  skin;
	protected Skin  defaultSkin;
	
	public AbstractGameScreen(Game game) {
		this.game = game;
		skin = new Skin(Gdx.files.internal(Constants.SKIN_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_UI));
		defaultSkin = new Skin(Gdx.files.internal(Constants.SKIN_LIBGDX_UI), new TextureAtlas(Constants.TEXTURE_ATLAS_LIBGDX_UI));
	}

	public abstract void render(float deltaTime);

	public abstract void resize(int width, int height);

	public abstract void show();

	public abstract void hide();

	public abstract void pause();

	public void resume() {
		Assets.instance.init(new AssetManager());
	}

	public void dispose() {
		Assets.instance.dispose();
		skin.dispose();
		defaultSkin.dispose();
	}

}
