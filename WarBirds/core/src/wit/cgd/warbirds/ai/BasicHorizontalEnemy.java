package wit.cgd.warbirds.ai;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.util.Constants;

public class BasicHorizontalEnemy extends AbstractEnemy {

    public BasicHorizontalEnemy(Level level, int multiplyer) {
        super(level, multiplyer);
        velocity = new Vector2(-1f, 0f);
        
        animation = Assets.instance.enemy[1].animationNormal;
        setAnimation(animation);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if (position.x <= -Constants.VIEWPORT_WIDTH/2+0.5f) velocity.x = 1f;
        else if (position.x >= Constants.VIEWPORT_WIDTH/2-0.5f) velocity.x = -1f;
        
        position.x = MathUtils.clamp(position.x,-Constants.VIEWPORT_WIDTH/2+0.5f, Constants.VIEWPORT_WIDTH/2-0.5f);
    }
    
}
