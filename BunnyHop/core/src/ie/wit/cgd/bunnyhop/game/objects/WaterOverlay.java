/**
 * 
 * @file        WaterOverlay.java
 * @author      Brian McCarthy, 20063914
 * @assignment  BunnyHop
 * @brief       Class for WaterOverlay object (extends the Abstract Game Object Class)
 * @notes       Code for rendering the WaterOverlay texture, no known BUGS or ISSUES
 *
 */

package ie.wit.cgd.bunnyhop.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ie.wit.cgd.bunnyhop.game.Assets;

public class WaterOverlay extends AbstractGameObject {

    private TextureRegion regWaterOverlay;
    private float length;

    public WaterOverlay(float length) {
        this.length = length;
        init();
    }

    private void init() {

        dimension.set(length * 10, 3);
        regWaterOverlay = Assets.instance.levelDecoration.waterOverlay;
        origin.x = -dimension.x / 2;
    }

    @Override
    public void render(SpriteBatch batch) {

        TextureRegion reg = null;
        reg = regWaterOverlay;
        batch.draw(reg.getTexture(), position.x + origin.x, position.y + origin.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, reg.getRegionX(), reg.getRegionY(), reg.getRegionWidth(), reg.getRegionHeight(), false, false);
    }
}