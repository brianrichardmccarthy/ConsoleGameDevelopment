package wit.cgd.warbirds.ai;

import com.badlogic.gdx.Gdx;
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

    public TextureRegion region;
    protected float timeShootDelay;

    public AbstractEnemy(Level level, int multiplyer, int texture) {
        super(level, multiplyer);
        init(texture);
    }

    public void init(int texture) {

        dimension.set(1, 1);
        
        setAnimation(Assets.instance.enemy[texture].animationNormal);
        
        // Center image on game object
        origin.set(dimension.x / 2, dimension.y / 2);
        timeShootDelay = 0;
        state = State.ACTIVE;
    }

    @Override
    public void update(float deltaTime) {

        super.update(deltaTime);
        
        if (state == State.ACTIVE) {
            if (timeShootDelay <= 0) shoot();
            else timeShootDelay -= deltaTime;
        }
    }

    public void shoot() {

        // get bullet
        Bullet bullet = level.enemyBulletPool.obtain();
        bullet.reset();
        bullet.position.set(position);

        // bullet.velocity = velocity;
        // bullet.rotation = rotation;
        
        level.enemyBullets.add(bullet);
        timeShootDelay = Constants.ENEMY_SHOOT_DELAY;

    }

    public void render(SpriteBatch batch) {

        if (state != State.ACTIVE && state != State.DYING) return;
        
        region = animation.getKeyFrame(stateTime, true);

        batch.draw(region.getTexture(), position.x - origin.x, position.y - origin.y, origin.x, origin.y, dimension.x,
            dimension.y, scale.x, scale.y, rotation, region.getRegionX(), region.getRegionY(), region.getRegionWidth(),
            region.getRegionHeight(), false, false);
    }

}
