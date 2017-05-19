/**
 *
 * @file        BasicVerticalEnemy
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Enemy plane that moves from of the top of the map to the end at a constant velocity
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;
import wit.cgd.warbirds.game.util.Constants;

public class BasicVerticalEnemy extends AbstractEnemy {

    public BasicVerticalEnemy(Level level, int multiplyer) {
        super(level, multiplyer, 0);
        velocity  = new Vector2(0f, -1f);
    }
    
    @Override
    public void update(float deltaTime) {
        
        if (state == State.DEAD || state == State.ASLEEP) return;
        
        super.update(deltaTime);
        // although the enemy does not move left to right, there is still a chane it will be half in screen
        // this prevents that from happening
        position.x = MathUtils.clamp(position.x,-Constants.VIEWPORT_WIDTH/2+0.5f,Constants.VIEWPORT_WIDTH/2-0.5f);
    }

}
