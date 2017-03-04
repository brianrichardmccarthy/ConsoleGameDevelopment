/**
 * 
 * @file        Goal.java
 * @author      Brian McCarthy, 20063914
 * @assignment  BunnyHop
 * @brief       Class for goal object (extends the Abstract Game Object Class)
 * @notes       Code for rendering the goal texture, as well as tracking if the goal is collected, no known BUGS or ISSUES
 *
 */

package ie.wit.cgd.bunnyhop.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ie.wit.cgd.bunnyhop.game.Assets;

public class Goal extends AbstractGameObject {

    private TextureRegion regGoal;
    public boolean collected;

    public Goal() {
        init();
    }

    private void init() {

        dimension.set(0.5f, 0.5f);
        regGoal = Assets.instance.goal.goal;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    @Override
    public void render(SpriteBatch batch) {

        if (collected) return;
        batch.draw(regGoal.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, regGoal.getRegionX(), regGoal.getRegionY(), regGoal.getRegionWidth(), regGoal.getRegionHeight(), false, false);
    }

}
