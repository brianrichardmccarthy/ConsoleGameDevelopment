/**
 *
 * @file        AbstractGameObject
 * @author      Brian McCarthy, 20063914
 * @assignment  Warbirds
 * @brief       Abstract object class
 * @notes       DESCRIPTION OF CODE, BUGS, FEATURES, ISSUES, ETC.
 *
 */
package wit.cgd.warbirds.game.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import wit.cgd.warbirds.game.util.Constants;

public abstract class AbstractGameObject {

	public Level 		level;
	
	public Vector2		position;
	public Vector2		dimension;
	public Vector2		origin;
	public Vector2		scale;
	public float		rotation;
	public Vector2		velocity;
	public Vector2		terminalVelocity;
	public Vector2		friction;
	public Vector2		acceleration;

	public float		stateTime;
	public Animation<TextureRegion>	animation;
	public Animation<TextureRegion> explosion;
	
	public float 		timeToDie;

	public int health;
	public int damage;
	public int multiplyer;
	
	public enum State {
		ASLEEP, // not yet in screen area 
		ACTIVE, // in screen area 
		DYING,	// outside screen area but has short time to enter it 
		DEAD	// to be removed from game
	}
	public State state;
	
	/**
	 * Constructor
	 * @param level
	 * @param multiplyer
	 */
	public AbstractGameObject(Level level, int multiplyer) {
		this.level = level;
		position = new Vector2();
		dimension = new Vector2(1, 1);
		origin = new Vector2();
		scale = new Vector2(1, 1);
		rotation = 0;
		velocity = new Vector2();
		terminalVelocity = new Vector2(1, 1);
		friction = new Vector2();
		acceleration = new Vector2();
		state = State.ASLEEP;
		this.multiplyer = multiplyer;
		health = Constants.BASE_HEALTH;
		damage = Constants.BASE_DAMAGE * multiplyer;
	}

	/**
	 * Update the motion, check if instance is still alive
	 * @param deltaTime
	 */
	public void update(float deltaTime) {
		
		if (state == State.ASLEEP) return;
		
		stateTime += deltaTime;
		

		// Move to new position
		position.x += velocity.x * deltaTime;
		position.y += velocity.y * deltaTime;
		
		if (health <= 0) {
		    state = State.DYING;
		    setAnimation(explosion);
		}
		
		if (state == State.DYING) {
		    if (!isInScreen()) state = State.ACTIVE;
			timeToDie -= deltaTime;
			if (timeToDie<0) state = State.DEAD;
		}
	}

	/**
	 * Changes the animation
	 * @param animation
	 */
	public void setAnimation(Animation<TextureRegion> animation) {
		this.animation = animation;
		stateTime = 0;
	}

	/**
	 * Render Method
	 * @param batch
	 */
	public abstract void render(SpriteBatch batch);

	/**
	 * Checks if this instance is inside the gamescreen
	 * @return
	 */
	public boolean isInScreen()  {
		return ((position.x>-Constants.VIEWPORT_WIDTH/2 && position.x<Constants.VIEWPORT_WIDTH/2) && 
				(position.y>level.start && position.y<level.end));
	}
}
