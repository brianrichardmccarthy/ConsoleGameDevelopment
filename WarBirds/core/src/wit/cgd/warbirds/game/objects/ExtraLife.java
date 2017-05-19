/**
 *
 * @file        ExtraLife
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Additional lives power up, gives the player another life upon collision.
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game.objects;

import com.badlogic.gdx.math.Vector2;

import wit.cgd.warbirds.game.Assets.Asset;

public class ExtraLife extends AbstractPowerUp {

    /**
     * Constructor
     * @param level
     * @param position
     * @param texture
     */
    public ExtraLife(Level level, Vector2 position, Asset texture) {
        super(level, position, texture);
        name = "ExtraLife";
    }

}
