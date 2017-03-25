package wit.cgd.xando;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import wit.cgd.xando.game.Assets;
import wit.cgd.xando.game.util.AudioManager;
import wit.cgd.xando.game.util.GamePreferences;
import wit.cgd.xando.screens.MenuScreen;

public class XandOMain extends Game {

    private static final String TAG = XandOMain.class.getName();

    @Override
    public void create() {
        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_DEBUG);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Load preferences for audio settings and start playing music
        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.song01);
        Gdx.app.debug(TAG, "Playing");
        // Start game at menu screen
        setScreen(new MenuScreen(this));
    }


}
