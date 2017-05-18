package wit.cgd.warbirds.game.objects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

import wit.cgd.warbirds.game.Assets.Asset;

public class ExtraLife extends AbstractPowerUp {

    public ExtraLife(Level level, Vector2 position, Asset texture) {
        super(level, position, texture);
        name = "ExtraLife";
    }

}
