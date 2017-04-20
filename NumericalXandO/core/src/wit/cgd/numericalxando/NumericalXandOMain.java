package wit.cgd.numericalxando;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;

import wit.cgd.numericalxando.game.Assets;
import wit.cgd.numericalxando.game.util.AudioManager;
import wit.cgd.numericalxando.game.util.GamePreferences;
import wit.cgd.numericalxando.screens.MenuScreen;

public class NumericalXandOMain extends Game {

    @SuppressWarnings("unused")
    private static final String TAG = NumericalXandOMain.class.getName();

    @Override
    public void create() {

        // Set Libgdx log level
        Gdx.app.setLogLevel(Application.LOG_NONE);
        // Load assets
        Assets.instance.init(new AssetManager());
        // Load preferences for audio settings and start playing music
        GamePreferences.instance.load();
        AudioManager.instance.play(Assets.instance.music.song01);
        // Start game at menu screen
        setScreen(new MenuScreen(this));
    }

}
