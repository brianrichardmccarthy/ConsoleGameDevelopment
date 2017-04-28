package wit.cgd.warbirds.game.objects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pool;

import wit.cgd.warbirds.ai.AbstractEnemy;
import wit.cgd.warbirds.ai.BasicEnemy;
import wit.cgd.warbirds.game.Assets;
import wit.cgd.warbirds.game.util.Constants;

public class Level extends AbstractGameObject {

    public static final String TAG = Level.class.getName();

    public Player player = null;
    public LevelDecoration levelDecoration;
    public float start;
    public float end;
    public Random random;
    public Random enemySpawn;
    private float islandTImer;
    private float enemyTimer;
    public Array<AbstractEnemy> enemies;
    
    private final String[] islands = {
            "islandBig",
            "islandSmall",
            "islandTiny"
    };

    public final Array<Bullet> bullets = new Array<Bullet>();

    public final Pool<Bullet> bulletPool = new Pool<Bullet>() {

        @Override
        protected Bullet newObject() {

            return new Bullet(level, Assets.instance.doubleBullet);
        }
    };
    
    public final Array<Bullet> enemyBullets = new Array<Bullet>();
    public final Pool<Bullet> enemyBulletPool = new Pool<Bullet>() {

        @Override
        protected Bullet newObject() {

            Bullet bullet = new Bullet(level, Assets.instance.bullet);
            
            bullet.velocity = new Vector2(bullet.velocity.x, -bullet.velocity.y);
            
            return bullet;
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

        enemySpawn = new Random((long) data.seed);
        enemies = new Array<AbstractEnemy>();
        
        Gdx.app.debug(TAG, "Number of Enemies <" + enemies.size + ">");
        
        Gdx.app.log(TAG, "islands . . . ");
        random = new Random(data.islands);
        for (int x = random.nextInt(100)+100; x >= 0; x--) {
            if (random.nextFloat() < 0.876) continue;
            levelDecoration.add(islands[random.nextInt(islands.length)], (((random.nextInt((Math.round(Constants.VIEWPORT_WIDTH*2))+1)) - Constants.VIEWPORT_WIDTH)/2), ((random.nextInt((Math.round(Constants.VIEWPORT_HEIGHT*2))+1)) - Constants.VIEWPORT_HEIGHT), random.nextInt(361));
        }

        position.set(0, 0);
        velocity.y = Constants.SCROLL_SPEED;
        state = State.ACTIVE;
        islandTImer = 1f;
        enemyTimer = 2f;

    }

    public void update(float deltaTime) {

        super.update(deltaTime);

        // limits for rendering
        start = position.y - scale.y * Constants.VIEWPORT_HEIGHT;
        end = position.y + scale.y * Constants.VIEWPORT_HEIGHT;

        player.update(deltaTime);

        for (Bullet bullet: bullets)
            bullet.update(deltaTime);
        
        for (Bullet bullet: enemyBullets) bullet.update(deltaTime);
        
        for (AbstractEnemy b: enemies) b.update(deltaTime);
        
        if (islandTImer <= 0) {
            if (random.nextFloat() >= 0.876) {
                levelDecoration.add(islands[random.nextInt(islands.length)], (((random.nextInt((Math.round(Constants.VIEWPORT_WIDTH*2))+1)) - Constants.VIEWPORT_WIDTH)/2), end, random.nextInt(361));
                islandTImer = 1f;
            }
        } else islandTImer -= deltaTime;
        
        if (enemyTimer <= 0) {
            if (enemySpawn.nextFloat() >= 0.75) {
                addEnemy("enemy_plane_green", (((enemySpawn.nextInt((Math.round(Constants.VIEWPORT_WIDTH*2))+1)) - Constants.VIEWPORT_WIDTH)/2), end, 0, 0);
                enemyTimer = 2f;
            }
        } else enemyTimer -= deltaTime;
    }

    public void render(SpriteBatch batch) {

        levelDecoration.render(batch);
        player.render(batch);
        for (Bullet bullet: bullets)
            bullet.render(batch);
        
        for (Bullet bullet: enemyBullets) bullet.render(batch);

        for (AbstractEnemy b: enemies) b.render(batch);
        
        // System.out.println("Bullets " + bullets.size);
    }

    public void addEnemy(String name, float x, float y, float rotation, int skill) {
        AbstractEnemy enemy = null;
        
        if (skill == 0) {
            enemy = new BasicEnemy(this);
        }

        enemy.player = player;
        enemy.origin.x = enemy.dimension.x/2; 
        enemy.origin.y = enemy.dimension.y/2; 
        enemy.position.set(x,y);
        enemy.rotation = rotation;
        enemies.add(enemy);
        
    }
    
}
