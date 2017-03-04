package ie.wit.cgd.bunnyhop.game;

import com.badlogic.gdx.Application.ApplicationType;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.math.Rectangle;

import ie.wit.cgd.bunnyhop.game.objects.BunnyHead;
import ie.wit.cgd.bunnyhop.game.objects.BunnyHead.JUMP_STATE;
import ie.wit.cgd.bunnyhop.game.objects.ExtraLives;
import ie.wit.cgd.bunnyhop.game.objects.Feather;
import ie.wit.cgd.bunnyhop.game.objects.Goal;
import ie.wit.cgd.bunnyhop.game.objects.GoldCoin;
import ie.wit.cgd.bunnyhop.game.objects.Rock;
import ie.wit.cgd.bunnyhop.util.CameraHelper;
import ie.wit.cgd.bunnyhop.util.Constants;

public class WorldController extends InputAdapter {

	@SuppressWarnings("unused")
    private static final String TAG = WorldController.class.getName();

    public CameraHelper cameraHelper;
    private String currentLevel;
    public Level level;
    public int lives;
    public int score;
    private int penalty;
    private float timeLeftGameOverDelay;
    private float timeLeftCompleteLevel;

    // Rectangles for collision detection
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();

    public WorldController() {
        init();
    }

    public float getTime() {

        return timeLeftCompleteLevel;
    }

    private void init() {

        Gdx.input.setInputProcessor(this);
        cameraHelper = new CameraHelper();
        timeLeftGameOverDelay = 0;
        lives = Constants.LIVES_START;
        currentLevel = Constants.LEVEL_01;
        initLevel();
    }

    public void update(float deltaTime) {

    	if (cameraHelper.hasTarget(level.bunnyHead)) timeLeftCompleteLevel -= deltaTime;
    	handleInputGame(deltaTime);
    	
    	if (isGameOver()) {
            timeLeftGameOverDelay -= deltaTime;
            if (timeLeftGameOverDelay < 0) initLevel();
        }
    	
        if (!isGameWon()) {
        	handleDebugInput(deltaTime);
            level.update(deltaTime);
            testCollisions();
            cameraHelper.update(deltaTime);
            if (!isGameOver() && isPlayerInWater()) {
                lives--;
                if (isGameOver()) timeLeftGameOverDelay = Constants.TIME_DELAY_GAME_OVER;
                else
                    initLevel();
            }
        }

    }

    private void initLevel() {

        score = 0;
        level = new Level(currentLevel);
        cameraHelper.setTarget(level.bunnyHead);
        penalty = Constants.LIVES_START - lives + 1;
        timeLeftCompleteLevel = Constants.TIME_COMPLETE_LEVELS / penalty;
    }

    private void handleInputGame(float deltaTime) {

        if (cameraHelper.hasTarget(level.bunnyHead)) {

            // Player Movement
            if (Gdx.input.isKeyPressed(Keys.LEFT)) {
                level.bunnyHead.velocity.x = -level.bunnyHead.terminalVelocity.x;
            } else if (Gdx.input.isKeyPressed(Keys.RIGHT)) {
                level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
            } else {
                // Execute auto-forward movement on non-desktop platform
                if (Gdx.app.getType() != ApplicationType.Desktop) {
                    level.bunnyHead.velocity.x = level.bunnyHead.terminalVelocity.x;
                }
            }
            
            if (Gdx.input.isKeyPressed(Keys.Y) && (isGameOver() || isGameWon())) {
            	if (currentLevel.equals(Constants.LEVEL_01)) currentLevel = Constants.LEVEL_02; 
            	initLevel();
            }
            else if (Gdx.input.isKeyPressed(Keys.N) && (isGameOver() || isGameWon())) Gdx.app.exit();

            // Bunny Jump
            if (Gdx.input.isTouched() || Gdx.input.isKeyPressed(Keys.SPACE)) level.bunnyHead.setJumping(true);
            else level.bunnyHead.setJumping(false);
        }
    }

    private void handleDebugInput(float deltaTime) {

        if (Gdx.app.getType() != ApplicationType.Desktop) return;

        // Camera Controls (move)
        if (!cameraHelper.hasTarget(level.bunnyHead)) {

            float camMoveSpeed = 5 * deltaTime;
            float camMoveSpeedAccelerationFactor = 5;
            if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
            if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
            if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
            if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
            if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);
        }

        // Camera Controls (move)
        float camMoveSpeed = 5 * deltaTime;
        float camMoveSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camMoveSpeed *= camMoveSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Keys.LEFT)) moveCamera(-camMoveSpeed, 0);
        if (Gdx.input.isKeyPressed(Keys.RIGHT)) moveCamera(camMoveSpeed, 0);
        if (Gdx.input.isKeyPressed(Keys.UP)) moveCamera(0, camMoveSpeed);
        if (Gdx.input.isKeyPressed(Keys.DOWN)) moveCamera(0, -camMoveSpeed);
        if (Gdx.input.isKeyPressed(Keys.BACKSPACE)) cameraHelper.setPosition(0, 0);

        // Camera Controls (zoom)
        float camZoomSpeed = 1 * deltaTime;
        float camZoomSpeedAccelerationFactor = 5;
        if (Gdx.input.isKeyPressed(Keys.SHIFT_LEFT)) camZoomSpeed *= camZoomSpeedAccelerationFactor;
        if (Gdx.input.isKeyPressed(Keys.COMMA)) cameraHelper.addZoom(camZoomSpeed);
        if (Gdx.input.isKeyPressed(Keys.PERIOD)) cameraHelper.addZoom(-camZoomSpeed);
        if (Gdx.input.isKeyPressed(Keys.SLASH)) cameraHelper.setZoom(1);
        
        if (Gdx.input.isKeyPressed(Keys.NUM_1)) {
            currentLevel = Constants.LEVEL_01;
            initLevel();
        }

        if (Gdx.input.isKeyPressed(Keys.NUM_2)) {
            currentLevel = Constants.LEVEL_02;
            initLevel();
        }
        
        if (Gdx.input.isKeyPressed(Keys.ESCAPE)) Gdx.app.exit();

    }

    private void moveCamera(float x, float y) {

        x += cameraHelper.getPosition().x;
        y += cameraHelper.getPosition().y;
        cameraHelper.setPosition(x, y);
    }

    @Override
    public boolean keyUp(int keycode) {

        if (keycode == Keys.R) {
            // Reset game world
            initLevel();
        } else if (keycode == Keys.ENTER) {
            // Toggle camera follow
            cameraHelper.setTarget(cameraHelper.hasTarget() ? null : level.bunnyHead);
        }
        return false;
    }

    private void onCollisionBunnyHeadWithRock(Rock rock) {

        BunnyHead bunnyHead = level.bunnyHead;
        float heightDifference = Math.abs(bunnyHead.position.y - (rock.position.y + rock.bounds.height));
        if (heightDifference > 0.25f) {
            boolean hitLeftEdge = bunnyHead.position.x > (rock.position.x + rock.bounds.width / 2.0f);
            if (hitLeftEdge) {
                bunnyHead.position.x = rock.position.x + rock.bounds.width;
            } else {
                bunnyHead.position.x = rock.position.x - bunnyHead.bounds.width;
            }
            return;
        }

        switch (bunnyHead.jumpState) {
            case GROUNDED:
                break;
            case FALLING:
            case JUMP_FALLING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                bunnyHead.jumpState = JUMP_STATE.GROUNDED;
                break;
            case JUMP_RISING:
                bunnyHead.position.y = rock.position.y + bunnyHead.bounds.height + bunnyHead.origin.y;
                break;
        }
    }

    private void onCollisionBunnyWithGoldCoin(GoldCoin goldcoin) {

        goldcoin.collected = true;
        score += goldcoin.getScore();
    }

    private void onCollisionBunnyWithFeather(Feather feather) {

        feather.collected = true;
        score += feather.getScore();
        level.bunnyHead.setFeatherPowerup(true);
    }

    private void onCollisionBunnyWithExtraLives(ExtraLives extraLive) {
    	extraLive.collected = true;
    	timeLeftCompleteLevel += 15.0f;
    	if (lives < Constants.LIVES_START) {
            lives++;
        }
    }

    private void onCollisionBunnyWithGoal(Goal goal) {

        if (score > 1000) {
            if (!goal.collected) {
                goal.collected = true;
            }
        }

    }

    private void testCollisions() {

        r1.set(level.bunnyHead.position.x, level.bunnyHead.position.y, level.bunnyHead.bounds.width, level.bunnyHead.bounds.height);

        // Test collision: Bunny Head <-> Rocks
        for (Rock rock : level.rocks) {
            r2.set(rock.position.x, rock.position.y, rock.bounds.width, rock.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyHeadWithRock(rock);
            // IMPORTANT: must do all collisions for valid
            // edge testing on rocks.
        }

        // Test collision: Bunny Head <-> Gold Coins
        for (GoldCoin goldCoin : level.goldCoins) {
            if (goldCoin.collected) continue;
            r2.set(goldCoin.position.x, goldCoin.position.y, goldCoin.bounds.width, goldCoin.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithGoldCoin(goldCoin);
            break;
        }

        // Test collision: Bunny Head <-> Feathers
        for (Feather feather : level.feathers) {
            if (feather.collected) continue;
            r2.set(feather.position.x, feather.position.y, feather.bounds.width, feather.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithFeather(feather);
            break;
        }

        for (ExtraLives extraLives : level.extraLives) {
            if (extraLives.collected) continue;
            r2.set(extraLives.position.x, extraLives.position.y, extraLives.bounds.width, extraLives.bounds.height);
            if (!r1.overlaps(r2)) continue;
            onCollisionBunnyWithExtraLives(extraLives);
        }
        
        // Test collision: Bunny Head <-> Goal
        r2.set(level.goal.position.x, level.goal.position.y, level.goal.bounds.width, level.goal.bounds.height);
        if (r1.overlaps(r2)) onCollisionBunnyWithGoal(level.goal);
    }

    public boolean isGameWon() {

        return level.goal.collected;
    }

    public boolean isGameOver() {

        return lives <= 0 || timeLeftCompleteLevel <= 0;
    }

    public boolean isPlayerInWater() {

        return level.bunnyHead.position.y < -5;
    }

}
