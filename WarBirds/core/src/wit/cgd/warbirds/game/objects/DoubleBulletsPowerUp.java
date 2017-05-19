/**
 *
 * @file        DoubleBulletsPowerUp
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Double bullets power up
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game.objects;

import com.badlogic.gdx.math.Vector2;

import wit.cgd.warbirds.game.Assets.Asset;

public class DoubleBulletsPowerUp extends AbstractPowerUp {

    public DoubleBulletsPowerUp(Level level, Vector2 position, Asset texture) {
        super(level, position, texture);
        name = "DoubleBullet";
    }
    
}
