/**
 *
 * @file        GamePreferences
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Player preferences class, stores music and sound volume
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {

    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instance = new GamePreferences();
    private Preferences prefs;

    public boolean music;
    public boolean sound;

    public float musicVolume;
    public float soundVolume;

    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    /**
     * Load preferences
     */
    public void load() {

        musicVolume = MathUtils.clamp(prefs.getFloat("musicVolume"), 0f, 1f);
        soundVolume = MathUtils.clamp(prefs.getFloat("soundVolume"), 0f, 1f);
        music = prefs.getBoolean("music", true);
        sound = prefs.getBoolean("sound", true);
    }

    /**
     * Save preferences
     */
    public void save() {

        prefs.putFloat("musicVolume", musicVolume);
        prefs.putFloat("soundVolume", soundVolume);
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.flush();
    }

}
