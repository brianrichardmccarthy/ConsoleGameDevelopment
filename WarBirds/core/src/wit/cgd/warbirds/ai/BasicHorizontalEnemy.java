package wit.cgd.warbirds.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.util.Constants;

public class BasicHorizontalEnemy extends AbstractEnemy {

    public BasicHorizontalEnemy(Level level, int multiplyer) {
        super(level, multiplyer, 1);
        velocity = new Vector2(-1f, 0f);
        
    }

    @Override
    public void update(float deltaTime) {
        
        if (state == State.DEAD) return;
        
        super.update(deltaTime);
        
        if (position.x <= -Constants.VIEWPORT_WIDTH/2+0.5f) velocity.x = 1f;
        else if (position.x >= Constants.VIEWPORT_WIDTH/2-0.5f) velocity.x = -1f;
        
        position.x = MathUtils.clamp(position.x,-Constants.VIEWPORT_WIDTH/2+0.5f, Constants.VIEWPORT_WIDTH/2-0.5f);
    }
    
}
