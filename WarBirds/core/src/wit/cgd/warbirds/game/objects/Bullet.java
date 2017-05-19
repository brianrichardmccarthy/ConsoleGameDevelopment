package wit.cgd.warbirds.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Pool.Poolable;

import wit.cgd.warbirds.game.Assets.Asset;
import wit.cgd.warbirds.game.util.Constants;


public class Bullet extends AbstractGameObject implements Poolable {

	public static final String TAG = Player.class.getName();
	
	private TextureRegion region;
	private Asset bullet;
	
	Bullet(Level level, Asset bullet, int damage) {
		super(level, damage);
		this.bullet = bullet;
		this.damage = damage;
		init();
	}
	
	Bullet(Level level, Asset bullet) {
        super(level, 1);
        this.bullet = bullet;
        init();
    }
	
	public void init() {
		dimension.set(0.5f, 0.5f);
				
		region = bullet.region;

		// Center image on game object
		origin.set(dimension.x / 2, dimension.y / 2);
		
		velocity.y = Constants.BULLET_SPEED;
	}
	
	public void setRegion(TextureRegion region) {
	    this.region = region;
	}
	
	@Override
	public void render(SpriteBatch batch) {
		batch.draw(region.getTexture(), position.x-origin.x, position.y-origin.y, origin.x, origin.y, 
				dimension.x, dimension.y, scale.x, scale.y, rotation, 
				region.getRegionX(), region.getRegionY(), region.getRegionWidth(), region.getRegionHeight(), 
				false, false);		
	}

	@Override
	public void reset() {
		//System.out.println("sdfsd");
		state = State.ACTIVE;
	}
}
