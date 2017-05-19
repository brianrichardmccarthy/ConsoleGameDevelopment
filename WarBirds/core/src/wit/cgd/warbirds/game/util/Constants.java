/**
 *
 * @file        Constants
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Game Constants
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game.util;

public class Constants {

    // Game world dimensions
    public static final float VIEWPORT_WIDTH = 8.0f;
    public static final float VIEWPORT_HEIGHT = 8.0f;

    // GUI dimensions
    public static final float VIEWPORT_GUI_WIDTH = 480.0f;
    public static final float VIEWPORT_GUI_HEIGHT = 740.0f;

    // atlas for all game sprites
    public static final String TEXTURE_ATLAS_GAME = "images/game.atlas";

    // Persistent storage files
    // Game setting (preferences + stats) files
    public static final String STATS = "xando.stats";
    public static final String PREFERENCES = "game.prefs";

    // Speed Constants (most relative to SCROLL_SPEED)
    public static final float SCROLL_SPEED = 1.0f;

    public static final float PLANE_H_SPEED = 5.0f;
    public static final float PLANE_MIN_V_SPEED = -3 * SCROLL_SPEED;
    public static final float PLANE_MAX_V_SPEED = 4 * SCROLL_SPEED;

    public static final float PLAYER_SHOOT_DELAY = 0.2f;
    public static final float BULLET_SPEED = 2.0f * PLANE_MAX_V_SPEED;

    public static final float ENEMY_SHOOT_DELAY = 0.5f;

    public static final float BULLET_DIE_DELAY = 1.2f;
    public static final float ENEMY_DIE_DELAY = 0.2f;
    
    public static final int BASE_HEALTH = 100;
    public static final int BASE_DAMAGE = 10;
    
    public static final int MAX_LIVES = 3;
    public static final float DOUBLE_BULLET_TIMER = 8f;
    
    // location of game specific skin and atlas
    public static final String  SKIN_UI                 = "images/ui.json";
    public static final String  TEXTURE_ATLAS_UI        = "images/ui.atlas";

    // location of libgdx default skin and atlas
    public static final String  SKIN_LIBGDX_UI          = "images/uiskin.json";
    public static final String  TEXTURE_ATLAS_LIBGDX_UI = "images/uiskin.atlas";
    
    public static final int BUTTON_PAD      = 15;
}
