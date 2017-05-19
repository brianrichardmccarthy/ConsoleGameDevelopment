/**
 *
 * @file        HealthPowerUp
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Health power up. Restore health upon collision
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game.objects;

import com.badlogic.gdx.math.Vector2;

import wit.cgd.warbirds.game.Assets.Asset;

public class HealthPowerUp extends AbstractPowerUp {

    /**
     * Constructor
     * @param level
     * @param position
     * @param texture
     */
    public HealthPowerUp(Level level, Vector2 position, Asset texture) {
        super(level, position, texture);
        name = "Health";
    }
    
}
