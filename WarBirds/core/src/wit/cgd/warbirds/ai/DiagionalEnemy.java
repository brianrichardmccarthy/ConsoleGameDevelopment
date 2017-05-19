/**
 *
 * @file        DiagionalEnemy
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Enemy that moves in both the x and y. This starts at either left to right or right to left and bounces off the left and right wall.
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.util.Constants;

public class DiagionalEnemy extends AbstractEnemy {

    /**
     * Constructor
     * @param level
     * @param multiplyer
     * @param x
     */
    public DiagionalEnemy(Level level, int multiplyer, float x) {
        super(level, multiplyer, 1);
        velocity = new Vector2(x, -1.5f);
    }

    /**
     * Updates and clamps position and shoot.
     */
    public void update(float deltaTime) {
        
        if (state == State.DEAD || state == State.ASLEEP) return;
        
        super.update(deltaTime);
        
        if (position.x <= -Constants.VIEWPORT_WIDTH/2+0.5f) velocity.x = 1f;
        else if (position.x >= Constants.VIEWPORT_WIDTH/2-0.5f) velocity.x = -1f;
        
        position.x = MathUtils.clamp(position.x,-Constants.VIEWPORT_WIDTH/2+0.5f, Constants.VIEWPORT_WIDTH/2-0.5f);
        
    }
    
}
