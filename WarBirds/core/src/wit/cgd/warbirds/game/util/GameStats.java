/**
 *
 * @file        GameStats
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Keeps track of the player deaths, and kills
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;

public class GameStats {

    public static final String TAG = GameStats.class.getName();
    public static final GameStats instance = new GameStats();
    private Preferences prefs;

    public int totalDeaths;
    public int totalKills;
    
    private GameStats() {
        prefs = Gdx.app.getPreferences(Constants.STATS);
    }

    /**
     * Load Stats
     */
    public void load() {
        totalDeaths = prefs.getInteger("totalDeaths", 0);
        totalKills = prefs.getInteger("totalKills", 0);
    }

    /**
     * Save Stats
     */
    public void save() {
        prefs.putInteger("totalDeaths", totalDeaths);
        prefs.putInteger("totalKills", totalKills);
        prefs.flush();
    }

    /**
     * Reset the Stats
     */
    public void reset() {
        totalDeaths = totalKills = 0;
    }
    
}
