package wit.cgd.warbirds.game.objects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pool;

import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.objects.enemys.Basic;
import wit.cgd.warbirds.game.util.Constants;

public class Level extends AbstractGameObject {

    public static final String TAG = Level.class.getName();

    public Player player = null;
    public LevelDecoration levelDecoration;
    public float start;
    public float end;
    public Random random;
    private float timer;
    private Array<Basic> enemies;
    
    private final String[] islands = {
            "islandBig",
            "islandSmall",
            "islandTiny"
    };

    public final Array<Bullet> bullets = new Array<Bullet>();

    public final Pool<Bullet> bulletPool = new Pool<Bullet>() {

        @Override
        protected Bullet newObject() {

            return new Bullet(level);
        }
    };
    
    public final Array<Bullet> enemyBullets = new Array<Bullet>();
    public final Pool<Bullet> enemyBulletPool = new Pool<Bullet>() {

        @Override
        protected Bullet newObject() {

            return new Bullet(level);
        }
    };
    
    /**
     * Simple class to store generic object in level.
     */
    public static class LevelObject {

        String name;
        int x;
        int y;
        float rotation;
        int state;
    }

    /**
     * Collection of all objects in level
     */
    public static class LevelMap {

        long islands;
        ArrayList<LevelObject> enemies;
        String name;
        float length;
        float seed;
    }

    public Level() {
        super(null);
        init();

    }

    private void init() {

        // player
        player = new Player(this);
        player.position.set(0, 0);

        levelDecoration = new LevelDecoration(this);

        // read and parse level map (form a json file)
        String map = Gdx.files.internal("levels/level-01.json").readString();

        Json json = new Json();
        json.setElementType(LevelMap.class, "enemies", LevelObject.class);
        LevelMap data = new LevelMap();
        data = json.fromJson(LevelMap.class, map);

        Gdx.app.log(TAG, "Data name = " + data.name);

        Gdx.app.log(TAG, "islands . . . ");
        random = new Random(data.islands);
        for (int x = random.nextInt(100)+100; x >= 0; x--) {
            if (random.nextFloat() < 0.876) continue;
            levelDecoration.add(islands[random.nextInt(islands.length)], (((random.nextInt((Math.round(Constants.VIEWPORT_WIDTH*2))+1)) - Constants.VIEWPORT_WIDTH)/2), ((random.nextInt((Math.round(Constants.VIEWPORT_HEIGHT*2))+1)) - Constants.VIEWPORT_HEIGHT), random.nextInt(361));
        }

        Gdx.app.log(TAG, "enemies . . . data.enemies <" + data.enemies.size() + ">");
        for (Object e: data.enemies) {
            LevelObject p = (LevelObject) e;
            Gdx.app.log(TAG, "type = " + p.name + "\tx = " + p.x + "\ty =" + p.y);
            Basic enemy = new Basic(this);
            enemy.position.set(p.x, p.y);
            enemies.add(enemy);
        }

        position.set(0, 0);
        velocity.y = Constants.SCROLL_SPEED;
        state = State.ACTIVE;
        timer = 1f;

    }

    public void update(float deltaTime) {

        super.update(deltaTime);

        // limits for rendering
        start = position.y - scale.y * Constants.VIEWPORT_HEIGHT;
        end = position.y + scale.y * Constants.VIEWPORT_HEIGHT;

        player.update(deltaTime);

        for (Bullet bullet: bullets)
            bullet.update(deltaTime);
        
        for (Basic b: enemies) b.update(deltaTime);
        
        if (timer <= 0) {
            if (random.nextFloat() < 0.876) return;
            levelDecoration.add(islands[random.nextInt(islands.length)], (((random.nextInt((Math.round(Constants.VIEWPORT_WIDTH*2))+1)) - Constants.VIEWPORT_WIDTH)/2), end, random.nextInt(361));
            timer = 1f;
        } else timer -= deltaTime;
    }

    public void render(SpriteBatch batch) {

        levelDecoration.render(batch);
        player.render(batch);
        for (Bullet bullet: bullets)
            bullet.render(batch);

        for (Basic b: enemies) b.render(batch);
        
        System.out.println("Bullets " + bullets.size);
    }

}
