package wit.cgd.warbirds.game.objects;

import java.util.ArrayList;
import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.Pool;

import wit.cgd.warbirds.ai.AbstractEnemy;
import wit.cgd.warbirds.ai.BasicVerticalEnemy;
import wit.cgd.warbirds.ai.Boss;
import wit.cgd.warbirds.ai.DiagionalEnemy;
import wit.cgd.warbirds.ai.BasicHorizontalEnemy;
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
    private float islandTimer;
    private float enemyTimer;
    public Array<AbstractEnemy> enemies;
    private String currentLevel;
    public int totalNumberOfEnemies;
    public int killedEnemies;
    private boolean bossSpawned;
    private AbstractEnemy boss;
    public Array<AbstractPowerUp> powerUps;
    
    boolean debug = false;
    
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
        public int numOfEnemies;
    }

    public Level(String level) {
        super(null, ((level.equals("levels/level-01.json")) ? 1 : 2));
        
        currentLevel = level;
        
        init();
    }

    private void init() {
        
        killedEnemies = 0;
        bossSpawned = false;
        // player
        player = new Player(this);
        player.position.set(0, 0);
        
        levelDecoration = new LevelDecoration(this);

        powerUps = new Array<AbstractPowerUp>();
        
        // read and parse level map (form a json file)
        String map = Gdx.files.internal(currentLevel).readString();

        Json json = new Json();
        json.setElementType(LevelMap.class, "enemies", LevelObject.class);
        LevelMap data = new LevelMap();
        data = json.fromJson(LevelMap.class, map);

        Gdx.app.log(TAG, "Data name = " + data.name);

        enemySpawn = new Random((long) data.seed);
        enemies = new Array<AbstractEnemy>();
        
        Gdx.app.log(TAG, "islands . . . ");
        random = new Random(data.islands);
        for (int x = random.nextInt(100)+100; x >= 0; x--) {
            if (random.nextFloat() < 0.876) continue;
            levelDecoration.add(islands[random.nextInt(islands.length)], (((random.nextInt((Math.round(Constants.VIEWPORT_WIDTH*2))+1)) - Constants.VIEWPORT_WIDTH)/2), ((random.nextInt((Math.round(Constants.VIEWPORT_HEIGHT*2))+1)) - Constants.VIEWPORT_HEIGHT), random.nextInt(361));
        }

        totalNumberOfEnemies = (int) data.numOfEnemies;
        Gdx.app.debug(TAG, "number <" + totalNumberOfEnemies + ">");
        position.set(0, 0);
        velocity.y = Constants.SCROLL_SPEED;
        state = State.ACTIVE;
        islandTimer = 1f;
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
        
        if (islandTimer <= 0) {
            if (random.nextFloat() >= 0.876) {
                levelDecoration.add(islands[random.nextInt(islands.length)], (((random.nextInt((Math.round(Constants.VIEWPORT_WIDTH*2))+1)) - Constants.VIEWPORT_WIDTH)/2), end, random.nextInt(361));
                islandTimer = 1f;
            }
        } else islandTimer -= deltaTime;
        
        if (enemyTimer <= 0) {
            if (enemySpawn.nextFloat() >= 0.75) {
                addEnemy((((enemySpawn.nextInt((Math.round(Constants.VIEWPORT_WIDTH*2))+1)) - Constants.VIEWPORT_WIDTH)/2), end, 0, enemySpawn.nextInt(3));
                enemyTimer = 2f;
            }
        } else enemyTimer -= deltaTime;
    }

    public void killedEnemy() {
        killedEnemies++;
    }
    
    public boolean isGameOver() {
        return bossSpawned && boss.health <= 0; 
    }
    
    public void addPowerUp(AbstractPowerUp powerUp) {
        powerUps.add(powerUp);
        return;
    }
    
    public void render(SpriteBatch batch) {

        levelDecoration.render(batch);
        player.render(batch);
        for (Bullet bullet: bullets)
            if (bullet.state == State.ACTIVE) bullet.render(batch);
        
        for (Bullet bullet: enemyBullets) 
            if (bullet.state == State.ACTIVE) bullet.render(batch);

        for (AbstractEnemy b: enemies) b.render(batch);
        
        for (AbstractGameObject b: powerUps) if (b.state == State.ACTIVE) b.render(batch);
        
    }

    public void addEnemy(float x, float y, float rotation, int skill) {
        
        if (bossSpawned) return;
        
        bossSpawned = killedEnemies >= totalNumberOfEnemies;
        
        // Gdx.app.debug(TAG, "Condition <" + (killedEnemies >= totalNumberOfEnemies) + "> killedEnemies <" + killedEnemies + "> totalNumberOfEnemies <" + totalNumberOfEnemies + ">" );
        
        AbstractEnemy enemy = null;
        
        if (bossSpawned) {
            enemy = new Boss(this, 1, player);
            boss = enemy;
        } else if (skill == 0) {
            enemy = new BasicVerticalEnemy(this, 1);
        } else if (skill == 1) {
            enemy = new BasicHorizontalEnemy(this, 1);
        } else {
            enemy = new DiagionalEnemy(this, 1, (enemySpawn.nextBoolean()) ? 1.5f: -1.5f);
        }

        enemy.origin.x = enemy.dimension.x/2;
        enemy.origin.y = enemy.dimension.y/2;
        enemy.position.set(x,y);
        enemy.rotation = rotation;
        enemy.explosion = Assets.instance.enemy[0].animationExplosionBig;
        enemies.add(enemy);
    }
    
}
