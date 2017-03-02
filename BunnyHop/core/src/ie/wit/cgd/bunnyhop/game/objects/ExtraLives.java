package ie.wit.cgd.bunnyhop.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import ie.wit.cgd.bunnyhop.game.Assets;

public class ExtraLives extends AbstractGameObject {

    private TextureRegion extraLive;
    public boolean collected;

    public ExtraLives() {
        init();
    }

    private void init() {

        dimension.set(0.5f, 0.5f);
        extraLive = Assets.instance.bunny.head;
        // Set bounding box for collision detection
        bounds.set(0, 0, dimension.x, dimension.y);
        collected = false;
    }

    @Override
    public void render(SpriteBatch batch) {

        if (collected) return;
        batch.draw(extraLive.getTexture(), position.x, position.y, origin.x, origin.y, dimension.x, dimension.y, scale.x, scale.y, rotation, extraLive.getRegionX(), extraLive.getRegionY(), extraLive.getRegionWidth(), extraLive.getRegionHeight(), false, false);
    }
}
