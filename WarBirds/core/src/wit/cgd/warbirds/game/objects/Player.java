package wit.cgd.warbirds.game.objects;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.util.Constants;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

public class Player extends AbstractGameObject {

	public static final String TAG = Player.class.getName();
	
	protected Animation<TextureRegion> animation;
	protected TextureRegion region;
	public TextureRegion bulletRegion;
	protected float timeShootDelay;
	
	public Player (Level level) {
		super(level, 2);
		init();
	}
	
	public void init() {
		dimension.set(1, 1);
				
		animation = Assets.instance.player.animationNormal;
		setAnimation(animation);

		bulletRegion = Assets.instance.bullet.region;
		
		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		timeShootDelay = 0;
		state = State.ACTIVE;
	}
	
	@Override
	public void update (float deltaTime) {
		super.update(deltaTime);
		position.x = MathUtils.clamp(position.x,-Constants.VIEWPORT_WIDTH/2+0.5f,Constants.VIEWPORT_WIDTH/2-0.5f);
		position.y = MathUtils.clamp(position.y,level.start+2, level.end-2);
		
		timeShootDelay -= deltaTime;
	}

	public void shoot() {

		if (timeShootDelay>0) return;
		
		// get bullet
		Bullet bullet = level.bulletPool.obtain();
		bullet.reset();
		bullet.position.set(position);
		
		bullet.setRegion(bulletRegion);
		
		// bullet.velocity = new Vector2(velocity.x * Constants.BULLET_SPEED, velocity.y * Constants.BULLET_SPEED);
		
		bullet.damage = damage;
		
		level.bullets.add(bullet);
		timeShootDelay = Constants.PLAYER_SHOOT_DELAY;

	}

	public void render (SpriteBatch batch) {
		
		region = animation.getKeyFrame(stateTime, true);

		batch.draw(region.getTexture(), position.x-origin.x, position.y-origin.y, origin.x, origin.y, 
			dimension.x, dimension.y, scale.x, scale.y, rotation, 
			region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), 
			false, false);
	}
	
}
