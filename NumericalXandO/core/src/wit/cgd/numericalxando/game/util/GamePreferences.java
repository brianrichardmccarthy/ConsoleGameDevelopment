package wit.cgd.numericalxando.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;

public class GamePreferences {

    public static final String TAG = GamePreferences.class.getName();

    public static final GamePreferences instance = new GamePreferences();
    private Preferences prefs;

    public boolean firstPlayerHuman;
    public boolean secondPlayerHuman;
    public boolean music;
    public boolean sound;

    public float firstPlayerSkill;
    public float secondPlayerSkill;
    public float musicVolume;
    public float soundVolume;

    private GamePreferences() {
        prefs = Gdx.app.getPreferences(Constants.PREFERENCES);
    }

    public void load() {

        firstPlayerSkill = MathUtils.clamp(prefs.getFloat("firstPlayerSkill"), 0f, 10f);
        secondPlayerSkill = MathUtils.clamp(prefs.getFloat("secondPlayerSkill"), 0f, 10f);
        musicVolume = MathUtils.clamp(prefs.getFloat("musicVolume"), 0f, 1f);
        soundVolume = MathUtils.clamp(prefs.getFloat("soundVolume"), 0f, 1f);
        firstPlayerHuman = prefs.getBoolean("firstPlayerHuman");
        secondPlayerHuman = prefs.getBoolean("secondPlayerHuman");
        music = prefs.getBoolean("music", true);
        sound = prefs.getBoolean("sound", true);
    }

    public void save() {

        prefs.putFloat("firstPlayerSkill", firstPlayerSkill);
        prefs.putFloat("secondPlayerSkill", secondPlayerSkill);
        prefs.putFloat("musicVolume", musicVolume);
        prefs.putFloat("soundVolume", soundVolume);
        prefs.putBoolean("sound", sound);
        prefs.putBoolean("music", music);
        prefs.putBoolean("firstPlayerHuman", firstPlayerHuman);
        prefs.putBoolean("secondPlayerHuman", secondPlayerHuman);
        prefs.flush();
    }

}
