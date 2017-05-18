package wit.cgd.warbirds.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;

import wit.cgd.warbirds.ai.AbstractEnemy;
import wit.cgd.warbirds.game.objects.AbstractGameObject;
import wit.cgd.warbirds.game.objects.AbstractGameObject.State;
import wit.cgd.warbirds.game.objects.AbstractPowerUp;
import wit.cgd.warbirds.game.objects.Bullet;
import wit.cgd.warbirds.game.objects.ExtraLife;
import wit.cgd.warbirds.game.objects.Level;
import wit.cgd.warbirds.game.util.CameraHelper;
import wit.cgd.warbirds.game.util.Constants;

public class WorldController extends InputAdapter {

    private static final String TAG = WorldController.class.getName();

    private Game game;
    public CameraHelper cameraHelper;
    public Level level;
    private String[] levels = { "levels/level-01.json", "levels/level-02.json", "levels/level-03.json" };
    public int lives;
    private int currentLevel = 0;

    public WorldController(Game game) {
        this.game = game;
        init();
    }

    private void init() {

        Gdx.input.setInputProcessor(this);
        level = new Level(levels[currentLevel]);
        cameraHelper = new CameraHelper();
        cameraHelper.setTarget(level);
        lives = Constants.MAX_LIVES;
    }
    
    private void init(String level) {
        
        if (level.equals("levels/level-01.json")) {
            currentLevel = 0;
        } else if (level.equals("levels/level-02.json")) {
            currentLevel = 1;
        } else {
            currentLevel = 2;
        }
        
        init();
        
    }

    public void update(float deltaTime) {

        if (level.isGameOver()) {

            if (++currentLevel < levels.length) {
                init();
            } else {
                Gdx.app.exit();
            }
        } else {
            handleDebugInput(deltaTime);
            handleGameInput(deltaTime);
            cameraHelper.update(deltaTime);
            level.update(deltaTime);
            checkBulletEnemyCollision();
            checkEnemyBulletPlayerCollision();
            checkEnemyPlayerCollision();
            checkEnemyPlanesCollisions();
            checkPlayerPowerUpCollisions();
            // cullObjects();
        }
    }

    /**
     * Remove object because they are out of screen bounds or because they have died
     */
    public void cullObjects() {

        // cull bullets 
        // traverse array backwards !!!
        for (int k = level.bullets.size; --k >= 0;) {
            Bullet it = level.bullets.get(k);
            if (it.state == Bullet.State.DEAD) {
                level.bullets.removeIndex(k);
                level.bulletPool.free(it);
            } else if (it.state == Bullet.State.ACTIVE && !isInScreen(it)) {
                it.state = Bullet.State.DYING;
                it.timeToDie = Constants.BULLET_DIE_DELAY;
            }
        }

        for (int x = level.enemies.size; --x >= 0;) {
            AbstractGameObject enemy = level.enemies.get(x);
            if (enemy.state == State.DEAD) level.enemies.removeIndex(x);
            else if (!isInScreen(enemy) && enemy.state == State.ACTIVE) {
                enemy.state = Bullet.State.DYING;
                enemy.timeToDie = Constants.ENEMY_DIE_DELAY;
            }
        }
        
        // power ups
    }

    public boolean isInScreen(AbstractGameObject gameObject) {

        return ( (gameObject.position.x > -Constants.VIEWPORT_WIDTH / 2 && gameObject.position.x < Constants.VIEWPORT_WIDTH / 2) && (gameObject.position.y > level.start && gameObject.position.y < level.end));
    }

    // Collision detection methods
    public void checkBulletEnemyCollision() {

        for (int x = level.enemies.size; --x >= 0;) {

            AbstractEnemy enemy = level.enemies.get(x);

            if (enemy.state != State.ACTIVE) continue;

            for (int y = level.bullets.size; --y >= 0;) {

                Bullet bullet = level.bullets.get(y);

                if (bullet.state != State.ACTIVE) continue;

                if (new Rectangle(enemy.position.x, enemy.position.y, enemy.dimension.x, enemy.dimension.y).overlaps(new Rectangle(
                    bullet.position.x, bullet.position.y, bullet.dimension.x, bullet.dimension.y))) {
                    enemy.health -= bullet.damage;
                    bullet.state = State.DEAD;
                    if (enemy.health <= 0) {
                        level.killedEnemy();
                        enemy.state = State.DYING;
                        enemy.timeToDie = Constants.ENEMY_DIE_DELAY;
                        if (level.enemySpawn.nextFloat() > 0.5f) {
                            level.addPowerUp(new ExtraLife(level, enemy.position, Assets.instance.extraLive));
                        }
                    }
                }
            }

        }

    }

    public void checkEnemyBulletPlayerCollision() {

        Rectangle player = new Rectangle(level.player.position.x, level.player.position.y, level.player.dimension.x,
            level.player.dimension.y);

        for (int x = level.enemyBullets.size; --x >= 0;) {

            Bullet b = level.enemyBullets.get(x);

            if (b.state != State.ACTIVE) continue;

            Rectangle bulletBox = new Rectangle(b.position.x, b.position.y, b.dimension.x, b.dimension.y);

            if (player.overlaps(bulletBox)) {
                // level.player.health -= b.damage;
                b.state = State.DEAD;
                b.timeToDie = Constants.BULLET_DIE_DELAY;
            }

        }

    }

    public void checkEnemyPlayerCollision() {

        Rectangle player = new Rectangle(level.player.position.x, level.player.position.y, level.player.dimension.x,
            level.player.dimension.y);

        for (int x = level.enemies.size; --x >= 0;) {

            AbstractEnemy b = level.enemies.get(x);

            if (b.state != State.ACTIVE) continue;

            Rectangle bulletBox = new Rectangle(b.position.x, b.position.y, b.dimension.x, b.dimension.y);

            if (player.overlaps(bulletBox)) {
                level.player.health /= 2;
                b.state = State.DYING;
                b.timeToDie = Constants.ENEMY_DIE_DELAY;
            }
        }
    }

    public void checkEnemyPlanesCollisions() {

        for (int current = level.enemies.size; --current >= 1;) {
            AbstractEnemy currentEnemy = level.enemies.get(current);

            if (currentEnemy.state != State.ACTIVE) continue;

            Rectangle currentEnemyBox = new Rectangle(currentEnemy.position.x, currentEnemy.position.y,
                currentEnemy.dimension.x, currentEnemy.dimension.y);
            for (int next = current - 1; --next >= 0;) {
                AbstractEnemy nextEnemy = level.enemies.get(next);

                if (nextEnemy.state != State.ACTIVE) continue;

                Rectangle nextEnemyBox = new Rectangle(nextEnemy.position.x, nextEnemy.position.y,
                    nextEnemy.dimension.x, nextEnemy.dimension.y);
                if (currentEnemyBox.overlaps(nextEnemyBox)) {
                    currentEnemy.state = State.DYING;
                    nextEnemy.state = State.DYING;
                    currentEnemy.timeToDie = Constants.ENEMY_DIE_DELAY;
                    nextEnemy.timeToDie = Constants.ENEMY_DIE_DELAY;
                }
            }
        }
    }
    
    public void checkPlayerPowerUpCollisions() {
        
        Rectangle player = new Rectangle(level.player.position.x, level.player.position.y, level.player.dimension.x,
            level.player.dimension.y);
        
        for (AbstractPowerUp powerUp : level.powerUps) {
            
            if (powerUp.state != State.ACTIVE) continue;
            
            Rectangle powerUpBox = new Rectangle(powerUp.position.x, powerUp.position.y, powerUp.dimension.x, powerUp.dimension.y);
            
            if (powerUpBox.overlaps(player)) {
                if (powerUp.name.equals("ExtraLife") && lives < Constants.MAX_LIVES) {
                    lives++;
                    powerUp.state = State.DEAD;
                }
            }
            
        }
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Keys.ESCAPE || keycode == Keys.BACK) {
            Gdx.app.exit();
        }
        return false;
    }

    private void handleGameInput(float deltaTime) {

        
        if (Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.D)) {
            // w, d
            level.player.rotation -=1;
        } else if (Gdx.input.isKeyPressed(Keys.W) && Gdx.input.isKeyPressed(Keys.A)) {
            // w, a
            level.player.rotation +=1;
        } else if (Gdx.input.isKeyPressed(Keys.S) && Gdx.input.isKeyPressed(Keys.D)) {
            // s, d
            level.player.rotation -=1;
        } else if (Gdx.input.isKeyPressed(Keys.S) && Gdx.input.isKeyPressed(Keys.A)) {
            // s, a
            level.player.rotation +=1;
        } else {
            if (Gdx.input.isKeyPressed(Keys.A)) {
                level.player.velocity.x = -Constants.PLANE_H_SPEED;
            } else if (Gdx.input.isKeyPressed(Keys.D)) {
                level.player.velocity.x = Constants.PLANE_H_SPEED;
            } else {
                level.player.velocity.x = 0;
            }
            
            if (Gdx.input.isKeyPressed(Keys.W)) {
                level.player.velocity.y = Constants.PLANE_MAX_V_SPEED;
            } else if (Gdx.input.isKeyPressed(Keys.S)) {
                level.player.velocity.y = Constants.PLANE_MIN_V_SPEED;
            } else {
                level.player.velocity.y = Constants.SCROLL_SPEED;
            }
            
        }
        
        
        if (Gdx.input.isKeyPressed(Keys.SPACE)) {
            level.player.shoot();
        }


    }

    private void handleDebugInput(float deltaTime) {

        if (Gdx.app.getType() != ApplicationType.Desktop) return;

        if (Gdx.input.isKeyPressed(Keys.ENTER)) {
            cameraHelper.setTarget(!cameraHelper.hasTarget() ? level : null);
        }

        if (!cameraHelper.hasTarget()) {
            // Camera Controls (move)
            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.reset();
        }

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
        if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
        
        if (Gdx.input.isKeyPressed(Keys.NUM_1)) init("levels/level-01.json");

        if (Gdx.input.isKeyPressed(Keys.NUM_2)) init("levels/level-02.json");

        if (Gdx.input.isKeyPressed(Keys.NUM_3)) init("levels/level-03.json");
        
    }

    private void moveCamera(float x, float y) {

        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {

        // TODO - implement touch pad type controls
        return true;
    }

}
