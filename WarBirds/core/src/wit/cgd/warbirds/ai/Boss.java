package wit.cgd.warbirds.ai;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.AbstractGameObject;
import wit.cgd.warbirds.game.objects.Bullet;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.util.Constants;

public class Boss extends AbstractEnemy {

    private AbstractGameObject player;
    private float distance;
    
    public Boss(Level level, int multiplyer, AbstractGameObject player) {
        super(level, multiplyer, 2);
        this.player = player;
        velocity = new Vector2(1f, 1f);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        
        if (position.x <= -Constants.VIEWPORT_WIDTH/2+0.5f) velocity.x = 1f;
        else if (position.x >= Constants.VIEWPORT_WIDTH/2-0.5f) velocity.x = -1f;
        
        if (position.y<level.start) {
            velocity.y = -2f;
        } else if (position.y>level.end) {
            velocity.y = 2f;
        }
        
        position.x = MathUtils.clamp(position.x,-Constants.VIEWPORT_WIDTH/2+0.5f,Constants.VIEWPORT_WIDTH/2-0.5f);
        position.y = MathUtils.clamp(position.y,level.start+2, level.end-2);
        
        facePlayer();
        changeVelocity(deltaTime);
        
    }
    
    public void facePlayer(){
        rotation = (float) (Math.atan2(player.position.y - this.position.y, player.position.x - this.position.x) * (180 / Math.PI)) + 90;
        distance = player.position.dst2(this.position);
    }
    
    private void changeVelocity(float deltaTime) {
        if (distance > 15f) {
            float speed = (distance > 50) ? 1.5f : 1f;
            if (rotation >= 180 && rotation <= 270) {
                // top left
                velocity = new Vector2(-speed, speed);
            }
            else if (rotation >= 90 && rotation <= 180) {
                // top right
                velocity = new Vector2(speed, speed);
            }
            else if (rotation >= -90 && rotation <= 0) {
                // bottom left
                velocity = new Vector2(-speed, -speed);
            }
            else if (rotation >= 0 && rotation <= 90) {
                // bottom right
                velocity = new Vector2(speed, -speed);
            }
        } else {
            velocity = new Vector2();
        }
    }
    
}
