package wit.cgd.warbirds.ai;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.AbstractGameObject;
import wit.cgd.warbirds.game.objects.Bullet;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;
import wit.cgd.warbirds.game.util.Constants;

public abstract class AbstractEnemy extends AbstractGameObject {

    public static final String TAG = AbstractEnemy.class.getName();

    protected Animation<TextureRegion> animation;
    protected TextureRegion region;
    protected float timeShootDelay;

    public AbstractGameObject player;
    
    public AbstractEnemy(Level level, int multiplyer) {
        super(level, multiplyer);
        init();
    }

    public void init() {

        dimension.set(1, 1);
        
        animation = Assets.instance.enemy[0].animationNormal;
        setAnimation(animation);
        
        // Center image on game object
        origin.set(dimension.x / 2, dimension.y / 2);
        timeShootDelay = 0;
        state = State.ACTIVE;
    }

    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);
        
        if (timeShootDelay <= 0) shoot();
        else timeShootDelay -= deltaTime;

        // rotation += (float) Math.atan2(player.position.y, player.position.x);
        
    }

    public void shoot() {

        // get bullet
        Bullet bullet = level.enemyBulletPool.obtain();
        bullet.reset();
        bullet.position.set(position);

        level.enemyBullets.add(bullet);
        timeShootDelay = Constants.ENEMY_SHOOT_DELAY;

    }

    public void render(SpriteBatch batch) {

        region = animation.getKeyFrame(stateTime, true);

        batch.draw(region.getTexture(), position.x - origin.x, position.y - origin.y, origin.x, origin.y, dimension.x,
            dimension.y, scale.x, scale.y, rotation, region.getRegionX(), region.getRegionY(), region.getRegionWidth(),
            region.getRegionHeight(), false, false);
    }

}
